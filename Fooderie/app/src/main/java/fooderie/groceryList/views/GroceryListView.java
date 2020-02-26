package fooderie.groceryList.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fooderie.MainActivity;
import com.example.fooderie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fooderie.groceryList.models.IngredientNotFoundException;
import fooderie.groceryList.models.UserGroceryListItem;
import fooderie.groceryList.viewModels.GroceryListViewModel;
import fooderie.groceryList.viewModels.Utility;
import fooderie.groceryList.models.Food;


public class GroceryListView extends AppCompatActivity {
    private GroceryListViewModel groceryListViewModel;

    HashMap<String, Food> storedGroceryList = new HashMap<String, Food>();
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
    //ProgressDialog progressDialog;
    ArrayList<String> grocList = new ArrayList<String>();
    ArrayList<String> gotList = new ArrayList<String>();

    String foodApiURL = "https://api.edamam.com/api/food-database/parser?ingr=basil&app_id=5bfe4f44&app_key=" +
            "d98b79a1c20ab328a1fc73311be5d1ee";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        groceryList = findViewById(R.id.groceryListDisplay);



        groceryListViewModel = ViewModelProviders.of(this).get(GroceryListViewModel.class);
        groceryListViewModel.getUserGroceryList().observe(this, new Observer<List<UserGroceryListItem>>() {
            @Override
            public void onChanged(List<UserGroceryListItem> userGroceryListItems) {
                //update our View later
                groceryListAdapter = new CustomAdapter(userGroceryListItems, getApplicationContext(), groceryListViewModel, GroceryListView.this);
                groceryList.setAdapter(groceryListAdapter);
                groceryListAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(groceryList);
                Toast.makeText(GroceryListView.this, "onChanged", Toast.LENGTH_SHORT).show();

            }
        });

        gotItList = findViewById(R.id.gotItListDisplay);
        addItem = findViewById(R.id.addGroceryItem);


        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.item_header, gotItList, false);
        clearGotItList = header.findViewById(R.id.clearGotItList);
        gotItHeader = header.findViewById(R.id.gotItHeader);
        gotItList.addHeaderView(header);

        requestQueue = Volley.newRequestQueue(this);

        //groceryListAdapter = new ArrayAdapter(GroceryListView.this, android.R.layout.simple_list_item_1, new ArrayList<String>(storedGroceryList.keySet())); //doesn't work

//        groceryListAdapter = new ArrayAdapter(GroceryListView.this, android.R.layout.simple_list_item_1, grocList);
//        groceryList.setAdapter(groceryListAdapter);

        gotItListAdapter = new ArrayAdapter(GroceryListView.this, android.R.layout.simple_list_item_1, gotList);
        gotItList.setAdapter(gotItListAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

//        groceryListAdapter.setOnDataChangeListener(new CustomAdapter.OnDataChangeListener() {
//            @Override
//            public void onDataChanged(String item) {
//                if (gotList.isEmpty()){
//                    gotItList.setVisibility(View.VISIBLE);
//                }
//                gotList.add(item);
//
//                //Dynamically set heights of the grocery and gotIt list
//                Utility.setListViewHeightBasedOnChildren(groceryList);
//                Utility.setListViewHeightBasedOnChildren(gotItList);
//
//                //Update View
//                groceryListAdapter.notifyDataSetChanged();
//                gotItListAdapter.notifyDataSetChanged();
//            }
//        });

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
//                    Toast.makeText(GroceryListView.this, "Problem Removing Element From Grocery List", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GroceryListView.this, "Problem Removing Element From Got It List", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(GroceryListView.this);
        builder.setTitle("Add to List");

        v = LayoutInflater.from(GroceryListView.this).inflate(R.layout.add_grocery_dialog, null, false);
        builder.setView(v);

        final SearchView grocerySearch = v.findViewById(R.id.addItem);
        grocerySearch.setFocusable(false);
        grocerySearch.setIconified(false);
        grocerySearch.clearFocus();

        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ProgressDialog progressDialog = new ProgressDialog(GroceryListView.this);
                progressDialog.setMessage("Please, wait while we find your ingredient");
                progressDialog.show();
                //((ViewGroup) v.getParent()).removeView(v);
//                progressDialog = new ProgressDialog(GroceryListView.this);
//                progressDialog.setTitle("Please, wait while we find your ingredient");
//                progressDialog.show();

                String query = grocerySearch.getQuery().toString();
                if (query.length()>= 1 && query != null){
                    boolean hasIngredientInDB = FetchIngredientFromDBBoolean(query);
                    if (hasIngredientInDB == false) {
                        FetchIngredientFromAPI(query); //Get Ingredients from API and save to DB
                        FetchIngredientFromDB(query);
                    } else {
                        FetchIngredientFromDB(query);
                    }

                } else if (query.length() == 0){
                    Toast.makeText(GroceryListView.this, "Nothing was added" + query, Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
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

    public boolean FetchIngredientFromDBBoolean(final String ingredient){
        List<Food> data = groceryListViewModel.getFoodByLabel(ingredient).getValue();
        if (data == null || data.isEmpty()){
            return false;
        }
        return true;
    }

    public void FetchIngredientFromDB(final String ingredient){
        List<Food> listFromDB = groceryListViewModel.getFoodByLabel(ingredient).getValue();
        return;
    }


    public void FetchIngredientFromAPI(final String ingredient){
        String foodApiDomainNPath = "https://api.edamam.com/api/food-database/parser";
        String queryParams;
        String appKey = "d98b79a1c20ab328a1fc73311be5d1ee";
        String appID = "5bfe4f44";

        String foodApiURLProper = foodApiDomainNPath + "?ingr=" + ingredient + "&app_id=" + appID + "&app_key=" + appKey;

        Log.e("API URL", foodApiURL);

        JsonObjectRequest objectReq = new JsonObjectRequest(
                Request.Method.GET,
                foodApiURLProper,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String text = response.getString("text"); //What user passes in and is parsed from API

                            JSONArray hintsIngredientsArray = response.getJSONArray("hints"); //The Array called "parsed"

                            if (hintsIngredientsArray.length() == 0){
                                Toast.makeText(GroceryListView.this, "Cannot find the ingredient: '" + text + "'", Toast.LENGTH_SHORT).show();
                                throw new IngredientNotFoundException("Cannot find the ingredient: " + text);
                            }

                            Gson gson = new GsonBuilder().create();

                            for (int i = 0; i < hintsIngredientsArray.length(); i++){
                                JSONObject ingredientParsed = hintsIngredientsArray.getJSONObject(i); //Gets the element returned at i position in the "hints" array
                                String stringRepOfFoodObj = ingredientParsed.getString("food");
                                Food f = gson.fromJson(stringRepOfFoodObj, Food.class);
                                groceryListViewModel.insert(f); //insert Food into DB
                                if (i == 0){
                                    UserGroceryListItem firstItem = new UserGroceryListItem(f.foodId, f.label);
                                    groceryListViewModel.insert(firstItem);
                                }
                            }


                            //groceryListAdapter.notifyDataSetChanged();// Not needed with custom adapter
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
