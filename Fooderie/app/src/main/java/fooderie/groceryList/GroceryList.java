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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

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
    ListView gotItList;
    TextView gotItHeader;
    TextView clearGotItList;
    //ArrayAdapter<String> groceryListAdapter;
    CustomAdapter groceryListAdapter;
    ArrayAdapter<String> gotItListAdapter;
    FloatingActionButton addItem;
    ProgressDialog dialog;
    ArrayList<String> grocList = new ArrayList<String>();
    ArrayList<String> gotList = new ArrayList<String>();

    String foodApiURL = "https://api.edamam.com/api/food-database/parser?ingr=basil&app_id=5bfe4f44&app_key=" +
            "d98b79a1c20ab328a1fc73311be5d1ee";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        groceryList = findViewById(R.id.groceryListDisplay);
        gotItList = findViewById(R.id.gotItListDisplay);
        addItem = findViewById(R.id.addGroceryItem);


        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.item_header, gotItList, false);
        clearGotItList = header.findViewById(R.id.clearGotItList);
        gotItHeader = header.findViewById(R.id.gotItHeader);
        gotItList.addHeaderView(header);


//        gotItHeader = new TextView(GroceryList.this);
//        gotItHeader.setText("Got It");
//
//        gotItList.addHeaderView(gotItHeader);

        dialog = new ProgressDialog(GroceryList.this);

        requestQueue = Volley.newRequestQueue(this);

        //groceryListAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, new ArrayList<String>(storedGroceryList.keySet())); //doesn't work

//        groceryListAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, grocList);
//        groceryList.setAdapter(groceryListAdapter);

        groceryListAdapter = new CustomAdapter(grocList, this.getBaseContext());
        groceryList.setAdapter(groceryListAdapter);

        gotItListAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, gotList);
        gotItList.setAdapter(gotItListAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        groceryListAdapter.setOnDataChangeListener(new CustomAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(String item) {
                if (gotList.isEmpty()){
                    gotItList.setVisibility(View.VISIBLE);
                }
                gotList.add(item);

                //Dynamically set heights of the grocery and gotIt list
                Utility.setListViewHeightBasedOnChildren(groceryList);
                Utility.setListViewHeightBasedOnChildren(gotItList);

                //Update View
                groceryListAdapter.notifyDataSetChanged();
                gotItListAdapter.notifyDataSetChanged();
            }
        });

//        groceryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String itemSelected = ((TextView) view).getText().toString(); // Gets the text of the selected item in list
//                if (itemSelected.equals(grocList.get(position))){
//                    //Make Got It only visible if something is deleted from it
//                    if (gotList.isEmpty()){
//                        gotItList.setVisibility(View.VISIBLE);
//                    }
//                    gotList.add(itemSelected);
//
//                    //Delete the element from groceryList
//                    grocList.remove(position);
//
//                    //Dynamically set heights of the grocery and gotIt list
//                    Utility.setListViewHeightBasedOnChildren(groceryList);
//                    Utility.setListViewHeightBasedOnChildren(gotItList);
//
//                    //Update View
//                    groceryListAdapter.notifyDataSetChanged();
//                    gotItListAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(GroceryList.this, "Problem Removing Element From Grocery List", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        gotItList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = ((TextView) view).getText().toString();
                if (itemSelected.equals(gotList.get(position - 1))){
                    grocList.add(itemSelected);
                    gotList.remove(itemSelected);

                    if (gotList.isEmpty()){
                        gotItList.setVisibility(View.INVISIBLE);
                    }

                    //Dynamically set heights of the grocery and gotIt list
                    Utility.setListViewHeightBasedOnChildren(groceryList);
                    Utility.setListViewHeightBasedOnChildren(gotItList);

                    //Update View
                    groceryListAdapter.notifyDataSetChanged();
                    gotItListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroceryList.this, "Problem Removing Element From Got It List", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearGotItList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gotList.isEmpty()) { return; }
                gotList.clear();
                gotItList.setVisibility(View.INVISIBLE);
                Utility.setListViewHeightBasedOnChildren(gotItList);
                gotItListAdapter.notifyDataSetChanged();
            }
        });

        gotItHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
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
                            grocList.add(recIngredient.ingredientName);
                            Utility.setListViewHeightBasedOnChildren(groceryList);
                            //groceryListAdapter.notifyDataSetChanged(); Not needed with custom adapter
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
