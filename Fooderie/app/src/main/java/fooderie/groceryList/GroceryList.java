package fooderie.groceryList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.fooderie.MainActivity;
import com.example.fooderie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fooderie.models.Food;
import fooderie.models.Hints;
import fooderie.models.Ingredient;
import fooderie.models.Measures;


public class GroceryList extends AppCompatActivity {
    HashMap<String, Ingredient> storedGroceryList = new HashMap<String, Ingredient>();
    View v;
    RequestQueue requestQueue;
    ListView groceryList;
    ArrayAdapter<String> arrayAdapter;
    FloatingActionButton addItem;
    ProgressDialog dialog;
    ArrayList<String> list = new ArrayList<String>();

    String foodApiURL = "https://api.edamam.com/api/food-database/parser?ingr=basil&app_id=5bfe4f44&app_key=" +
            "d98b79a1c20ab328a1fc73311be5d1ee";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        groceryList = findViewById(R.id.groceryListDisplay);
        addItem = findViewById(R.id.addGroceryItem);

        dialog = new ProgressDialog(GroceryList.this);

        requestQueue = Volley.newRequestQueue(this);

        //arrayAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, new ArrayList<String>(storedGroceryList.keySet()));
        arrayAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, list);
        groceryList.setAdapter(arrayAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        groceryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = ((TextView) view).getText().toString(); // Gets the text of the selected item in list
                if (itemSelected.equals(list.get(position))){
                    //Make this send user a prompt to either edit or remove the element
                    list.remove(position);
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroceryList.this, "Problem Removing Element", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addIngredient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroceryList.this);
        builder.setTitle("Add to List");

        v = LayoutInflater.from(GroceryList.this).inflate(R.layout.add_grocery_dialog, null, false);
        builder.setView(v);

        final SearchView grocerySearch = v.findViewById(R.id.addItem);
        grocerySearch.setFocusable(false);
        grocerySearch.setIconified(false);
        grocerySearch.clearFocus();

        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String query = grocerySearch.getQuery().toString();

                if (query.length()>= 1 && query != null){
                    FetchIngredient(query);
                } else if (query.length() == 0){
                    Toast.makeText(GroceryList.this, "Nothing was added" + query, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    public void FetchIngredient(final String ingredient){

        String foodApiDomainNPath = "https://api.edamam.com/api/food-database/parser";
        String queryParams;
        String appKey = "d98b79a1c20ab328a1fc73311be5d1ee";
        String appID = "5bfe4f44";

        String foodApiURLProper = foodApiDomainNPath + "?ingr=" + ingredient + "&app_id=" + appID + "&app_key=" + appKey;

        Log.e("API URL", foodApiURL);

        Ingredient recIngredient = new Ingredient();

        dialog.setMessage("Please, wait while we find your ingredient");
        dialog.show();

        JsonObjectRequest objectReq = new JsonObjectRequest(
                Request.Method.GET,
                foodApiURLProper,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String text = response.getString("text"); //What user passes in and is parsed from API

                            JSONArray ingredientParsed = response.getJSONArray("parsed"); //The Array called "parsed"

                            if (ingredientParsed.length() == 0){
                                dialog.dismiss();
                                Toast.makeText(GroceryList.this, "Cannot find the ingredient: '" + text + "'", Toast.LENGTH_SHORT).show();
                                throw new IngredientNotFoundException("Cannot find the ingredient: " + text);
                            }

                            JSONObject defReturnObj = ingredientParsed.getJSONObject(0); //Gets the only element returned at 0 position in the "parsed" array
                            JSONObject ingredientObj = defReturnObj.getJSONObject("food"); // The object at 0 in "parsed" array is called "food"
                            String ingredientName = ingredientObj.getString("label"); // Gets the parsed name by the API of the ingredient we're looking for from the response

                            String stringRespOfFoodObject = defReturnObj.getString("food");

                            ingredientName = ingredientName.toLowerCase();
                            ingredientName = ingredientName.substring(0, 1).toUpperCase() + ingredientName.substring(1);


                            Gson gson = new GsonBuilder().create();
                            recIngredient.ingredientName = ingredientName;
                            recIngredient.ingredientParsed = gson.fromJson(stringRespOfFoodObject, Food.class);

                            JSONArray hintsParsed = response.getJSONArray("hints");
                            for (int i = 0; i < hintsParsed.length(); i++){
                                JSONObject foodNMeasures = hintsParsed.getJSONObject(i);

                                String food = foodNMeasures.getString("food");
                                String measures = foodNMeasures.getString("measures");

                                Food f = gson.fromJson(food, Food.class);
                                Measures[] m = gson.fromJson(measures, Measures[].class);

                                Hints h = new Hints(f, m);
                                recIngredient.hintsParsed.add(h);
                            }

                            storedGroceryList.put(recIngredient.ingredientName, recIngredient);
                            list.add(recIngredient.ingredientName);
                            arrayAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            return;

                        } catch (IngredientNotFoundException ne){
                            Log.e("INGRDIENT NOT FOUND", ne.toString());
                        }
                        catch (Exception e) {
                            Log.e("ASYNCTASK ERROR", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API ERROR response", error.toString());
                    }
                });

        requestQueue.add(objectReq);
    }
}
