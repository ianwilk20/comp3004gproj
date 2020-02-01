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

        requestQueue = Volley.newRequestQueue(this);

        //arrayAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, new ArrayList<String>(myGroceryList.list.keySet()));
        arrayAdapter = new ArrayAdapter(GroceryList.this, android.R.layout.simple_list_item_1, list);
        groceryList.setAdapter(arrayAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
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
                    //FetchIngredientAsync fetchIngredientTask = new FetchIngredientAsync();
                    //fetchIngredientTask.execute(query);
                    list.add(query);
                    arrayAdapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(GroceryList.this, "We could not find your item" + query, Toast.LENGTH_SHORT).show();
//                    }
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

        final Ingredient recIngredient = new Ingredient();


        //Show progress dialog meanwhile
        //ProgressBar progressDialog = new ProgressBar(getCallingActivity());
    }

    private class FetchIngredientAsync extends AsyncTask<String, Integer, String> {
        public Ingredient recIngredient;
        public String foodApiDomainNPath = "https://api.edamam.com/api/food-database/parser";
        public String queryParams;
        private String appKey = "d98b79a1c20ab328a1fc73311be5d1ee";
        private String appID = "5bfe4f44";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            dialog.setMessage("Please, wait while we find your ingredient");
            dialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String foodApiURLProper = foodApiDomainNPath + "?ingr=" + strings[0] + "&app_id=" + appID + "&app_key=" + appKey;

            JsonObjectRequest objectReq = new JsonObjectRequest(
                    Request.Method.GET,
                    foodApiURLProper,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String text = response.getString("text");
                                text = text.toLowerCase();
                                text = text.substring(0, 1).toUpperCase() + text.substring(1);

                                JSONArray ingredientParsed = response.getJSONArray("parsed");
                                JSONObject defReturnObj = ingredientParsed.getJSONObject(0);
                                String ingredientObj = defReturnObj.getString("food");

                                Gson gson = new GsonBuilder().create();
                                recIngredient.ingredientName = text;
                                recIngredient.ingredientParsed = gson.fromJson(ingredientObj, Food.class);

                                JSONArray hintsParsed = response.getJSONArray("hints");
                                for (int i = 0; i < hintsParsed.length(); i++){
                                    JSONObject foodNMeasures = hintsParsed.getJSONObject(i);

                                    JSONObject food = foodNMeasures.getJSONObject("food");
                                    String foodObj = food.getString("food");
                                    JSONObject measures = foodNMeasures.getJSONObject("measures");
                                    String measuresObj = measures.getString("measures");

                                    Food f = gson.fromJson(foodObj, Food.class);
                                    Measures m = gson.fromJson(measuresObj, Measures.class);

                                    Hints h = new Hints(f, m);
                                    recIngredient.hintsParsed.add(h);
                                }

                                storedGroceryList.put(recIngredient.ingredientName, recIngredient);

                                return;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("API ERROR response", error.toString());
                        }
                    });

            //Add request to queue
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            requestQueue.add(objectReq);
            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(GroceryList.this, s, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
}
