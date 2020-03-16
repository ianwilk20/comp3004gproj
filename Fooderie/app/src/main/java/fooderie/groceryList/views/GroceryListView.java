package fooderie.groceryList.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fooderie.groceryList.VolleySingleton;
import fooderie.groceryList.models.IngredientNotFoundException;
import fooderie.groceryList.models.UserGroceryListItem;
import fooderie.groceryList.viewModels.GroceryListViewModel;
import fooderie.groceryList.viewModels.Utility;
import fooderie.groceryList.models.Food;
import fooderie.recipeBrowser.models.Recipe;

import org.apache.commons.lang3.text.WordUtils;


public class GroceryListView extends AppCompatActivity {
    private GroceryListViewModel groceryListViewModel;

    View v;
    RequestQueue requestQueue;
    TextView shoppingModeBanner;
    ListView groceryList;
    ImageButton saveButton;
    ImageButton optionsButton;
    ImageButton mealPlanRecipes;
    CustomAdapter groceryListAdapter;
    Button addItem;
    ProgressDialog progressDialog;
    ProgressDialog mealPlanAddProgressDialog;
    ProgressDialog noInternetDialog;

    String foodApiDomainNPath = "https://api.edamam.com/api/food-database/parser";
    String queryParams;
    String appKey = "d98b79a1c20ab328a1fc73311be5d1ee";
    String appID = "5bfe4f44";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        //Getting tagged items from activity
        groceryList = findViewById(R.id.groceryListDisplay);
        optionsButton = findViewById(R.id.optionsButton);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        //requestQueue = Volley.newRequestQueue(this);
        addItem = findViewById(R.id.addGroceryItem);
        saveButton = findViewById(R.id.saveButton);
        mealPlanRecipes = findViewById(R.id.addRecipesToGroceries);
        shoppingModeBanner = findViewById(R.id.shoppingModeBanner);
        RelativeLayout emptyListLayout = (RelativeLayout) findViewById(R.id.emptyGroceryListPicture);

        groceryListViewModel = ViewModelProviders.of(this).get(GroceryListViewModel.class);
        groceryListViewModel.getUserGroceryList().observe(this, new Observer<List<UserGroceryListItem>>() {
            @Override
            public void onChanged(List<UserGroceryListItem> userGroceryListItems) {
                if (userGroceryListItems.size() == 0){
                        emptyListLayout.setVisibility(View.VISIBLE);
                } else { emptyListLayout.setVisibility(View.INVISIBLE);};
                groceryListAdapter = new CustomAdapter(userGroceryListItems, getApplicationContext(), groceryListViewModel, GroceryListView.this);
                groceryList.setAdapter(groceryListAdapter);
                //groceryListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        });

        groceryListViewModel.getAllFoodItems().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                //Toast.makeText(GroceryListView.this, "Food Table Changed", Toast.LENGTH_SHORT).show();
            }
        });

        groceryListViewModel.getAllRecipesForNextWeek().removeObservers(this);
        groceryListViewModel.getAllRecipesForNextWeek().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
//                populateGroceryListWithNWMealPlanRecipes(true);
//                mealPlanAddProgressDialog.dismiss();
            }
        });


        LayoutInflater inflater = getLayoutInflater();
        progressDialog = new ProgressDialog(GroceryListView.this);
        mealPlanAddProgressDialog = new ProgressDialog(GroceryListView.this);
        noInternetDialog = new ProgressDialog(GroceryListView.this);

        if (!isNetworkAvailable()){
            shoppingModeBanner.setVisibility(View.VISIBLE);
            AlertDialog.Builder finalDialog = new AlertDialog.Builder(GroceryListView.this)
                    .setTitle("Shopping Mode Enabled")
                    .setMessage("Shopping mode is enabled because you are not connected to the internet. Please note that we cannot verify the validity of ingredients unless they are added with an active internet connection.")
                    .setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            finalDialog.show();
        } else {
            shoppingModeBanner.setVisibility(View.INVISIBLE);
        }

        final PopupMenu dropDownSaveMenu = new PopupMenu(this, saveButton);
        final Menu save_menu = dropDownSaveMenu.getMenu();
        save_menu.add(0, 0, 0, "Copy to Clipboard");
        save_menu.add(0, 1, 0, "Email Grocery List");
        save_menu.add(0,2,0,"Send to a friend");

        dropDownSaveMenu.getMenuInflater().inflate(R.menu.save_menu, save_menu);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownSaveMenu.show();
            }
        });

        final PopupMenu dropDownOptionMenu = new PopupMenu(this, optionsButton);
        final Menu options_menu = dropDownOptionMenu.getMenu();
        options_menu.add(0, 0, 0, "Clear items in pantry");
        options_menu.add(0, 1, 0, "Clear all items");

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownOptionMenu.show();
            }
        });

        dropDownSaveMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String body = "";

                LiveData<List<UserGroceryListItem>> grocListFromDB = groceryListViewModel.getUserGroceryList();
                List<UserGroceryListItem> grocList = grocListFromDB.getValue();

                for (UserGroceryListItem i : grocList){

                    body += "• " + i.getFood_name();
                    body += i.getQuantity() == null || i.getQuantity().isEmpty() ? "" : ", Quantity: " + i.getQuantity();
                    body += i.getNotes() == null || i.getNotes().isEmpty()? "" : ", Notes: " + i.getNotes();
                    body += i.getDepartment() == null || i.getDepartment().isEmpty() ? "" : ", Department: " + i.getDepartment();
                    body += "\n";
                }


                switch (item.getItemId()){
                    case 0:
                        //Clipboard
                        Toast.makeText(GroceryListView.this, "Grocery List Copied to Clipboard", Toast.LENGTH_SHORT).show();
                        final android.content.ClipboardManager clipboardMan = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                        ClipData groceryListClipData = ClipData.newPlainText("Source Text", body);
                        clipboardMan.setPrimaryClip(groceryListClipData);
                        return true;
                    case 1:
                        //Email
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { ""});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Grocery List");

                        body += "\nThank you for using Fooderie :) \n";

                        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

                        startActivity(Intent.createChooser(emailIntent, ""));
                        return true;
                    case 2:
                        //SMS
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setData(Uri.parse("sms:"));
                        smsIntent.putExtra("sms_body", body);
                        startActivity(smsIntent);
                        return true;
                }
                return false;
            }
        });

        dropDownOptionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:
                        groceryListAdapter.clearCrossedOffItems();
                        return true;
                    case 1:
                        //clear all items - prompt first
                        groceryListAdapter.clearAllItems();
                        return true;
                }
                return false;
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        mealPlanRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateGroceryListWithNWMealPlanRecipes(false);
            }
        });
    }

    public void populateGroceryListWithNWMealPlanRecipes(boolean onDataChanged) {
        //Toast.makeText(GroceryListView.this, "In populate", Toast.LENGTH_LONG).show();
        AlertDialog alertDialog = new AlertDialog.Builder(GroceryListView.this).create();
        alertDialog.setTitle("Add Items to Grocery List");
        if (onDataChanged){
            alertDialog.setMessage("Next week's meal plan has new ingredients. Would you like to add them to the grocery list?");
        } else {
            alertDialog.setMessage("Would you like to add next weeks recipe ingredients to the grocery list?");
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                LiveData<List<Recipe>> dbNextWeekRecipes = groceryListViewModel.getAllRecipesForNextWeek();
                List<Recipe> nextWeeksRecipeIngredients = dbNextWeekRecipes.getValue();

                mealPlanAddProgressDialog.setMessage("Please, wait while we add your meal planner ingredients to your grocery list");
                mealPlanAddProgressDialog.show();

                if (nextWeeksRecipeIngredients != null && nextWeeksRecipeIngredients.size() != 0){
                    for (Recipe r : nextWeeksRecipeIngredients){
                        for (String s : r.theIngredients){

                            //Check if an ingredient is in the grocery list
                            List<UserGroceryListItem> groceryListItem = FetchIngredientFromGroceryList(s);
                            if (groceryListItem != null && groceryListItem.size() != 0){
                                String foodId = "food_" + UUID.randomUUID().toString();
                                UserGroceryListItem fabricatedItem = new UserGroceryListItem(foodId, s);
                                fabricatedItem.setNotes("From Your Weekly Meal Plan");
                                groceryListViewModel.insert(fabricatedItem);
                                continue;
                            } //Found match in grocery list will add duplicate

                            //Otherwise check if it's in the food table
                            Food itemFromDB = null; //Search DB
                            itemFromDB = FetchIngredientByLabelFromDB(s);
                            if (itemFromDB != null) { //Found in Food DB will use that object
                                UserGroceryListItem fabricatedItem = new UserGroceryListItem(itemFromDB.foodId, itemFromDB.label);
                                fabricatedItem.setNotes("From Your Week Meal Plan");
                                groceryListViewModel.insert(fabricatedItem);
                                continue;
                            }

                            if (!isNetworkAvailable()) {
                                String foodId = "food_" + UUID.randomUUID().toString();
                                UserGroceryListItem item = new UserGroceryListItem(foodId, s);
                                item.setNotes("From Your Week Meal Plan");
                                groceryListViewModel.insert(item);
                            } else {
                                // Fetch From API if not there...
                                // Create our own entry into GroceryList Table fabricating food_id
                                SeqAPIFetchAndInsert(s);
                            }
                        }
                    }
                }
                else {
                    AlertDialog.Builder finalDialog = new AlertDialog.Builder(GroceryListView.this)
                    .setTitle("Empty Meal Plan")
                    .setMessage("Next week's meal plan is empty. To import ingredients from recipes create a weekly meal plan, add recipes to it then add it to the schedule.")
                    .setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = finalDialog.create();
                    alert.show();
                }

                mealPlanAddProgressDialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        alertDialog.show();
    }

    private void addIngredient() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GroceryListView.this);
        //builder.setTitle("Add to List");

        v = LayoutInflater.from(GroceryListView.this).inflate(R.layout.add_grocery_dialog, null, false);
        builder.setView(v);

        final SearchView grocerySearch = v.findViewById(R.id.addItem);
        grocerySearch.setFocusable(false);
        grocerySearch.setIconified(false);
        grocerySearch.clearFocus();

        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage("Please, wait while we find your ingredient");
                progressDialog.show();
                String query = grocerySearch.getQuery().toString();
                if (query.length()>= 1 && query != null){
                    //Try to find in grocery list (maybe trying to add a duplicate)
                    List<UserGroceryListItem> ugiFromDB = FetchIngredientFromGroceryList(query);
                    if (ugiFromDB != null && ugiFromDB.size() != 0 && ugiFromDB.get(0) != null){
                        String newFoodId = ugiFromDB.get(0).getFood_id() + UUID.randomUUID().toString();
                        UserGroceryListItem alteredIngredient = new UserGroceryListItem(newFoodId, ugiFromDB.get(0).getFood_name());
                        groceryListViewModel.insert(alteredIngredient);
                        return;
                    }

                    //Try to find ingredient by label in our Food Table in DB
                    Food foodReturnedFromDB = FetchIngredientByLabelFromDB(query);
                    if (foodReturnedFromDB != null) {
                        UserGroceryListItem newItem = new UserGroceryListItem(foodReturnedFromDB.foodId, foodReturnedFromDB.label);
                        groceryListViewModel.insert(newItem);
                        return;
                    }
                    if (!isNetworkAvailable()){
                        String foodId = "food_" + UUID.randomUUID().toString();
                        UserGroceryListItem fabricatedItem = new UserGroceryListItem(foodId, query);
                        fabricatedItem.setNotes("Added through shopping mode");
                        groceryListViewModel.insert(fabricatedItem);
                    } else {
                        //If not in our DB get it from the API, save to Food table in DB and insert first occurrence into Grocery List
                        FetchIngredientFromAPI(query); //Get Ingredients from API and save to DB
                    }

                } else if (query.length() == 0){
                    createDialog("Please provide an ingredient to search.");
                }
            }
        });

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialogInterface){

            }
        });

        builder.show();
    }

    public Food FetchIngredientByLabelFromDB(String ingredient) {
        GetFoodByLabelAsyncTask getFoodByLabel = new GetFoodByLabelAsyncTask();
        getFoodByLabel.execute(ingredient);
        Food fromDB = null;
        try {
            fromDB = getFoodByLabel.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (fromDB == null){
            return null;
        } else {
            return fromDB;
        }
    }

    public List<UserGroceryListItem> FetchIngredientFromGroceryList(String ingredient){
        GetGroceryItemByLabelAsyncTask getGroceryItemByLabel = new GetGroceryItemByLabelAsyncTask();
        getGroceryItemByLabel.execute(ingredient);
        List<UserGroceryListItem> fromDB = null;
        try {
            fromDB = getGroceryItemByLabel.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (fromDB == null){
            return null;
        } else {
            return fromDB;
        }
    }

    public void FetchIngredientFromAPI(final String ingredient){

        String foodApiURLProper = foodApiDomainNPath + "?ingr=" + ingredient + "&app_id=" + appID + "&app_key=" + appKey;
        foodApiURLProper = foodApiURLProper.replaceAll("%", " ");


        Log.e("API URL", foodApiURLProper);

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
                                throw new IngredientNotFoundException("Cannot find the ingredient: " + text);
                            }

                            Gson gson = new GsonBuilder().create();

                            for (int i = 0; i < hintsIngredientsArray.length(); i++){
                                JSONObject ingredientParsed = hintsIngredientsArray.getJSONObject(i); //Gets the element returned at i position in the "hints" array
                                String stringRepOfFoodObj = ingredientParsed.getString("food");
                                Food f = gson.fromJson(stringRepOfFoodObj, Food.class);
                                f.label = WordUtils.capitalizeFully(f.getLabel());
                                groceryListViewModel.insert(f); //insert Food into DB
                                if (i == 0){
                                    UserGroceryListItem firstItem = new UserGroceryListItem(f.foodId, f.label);
                                    groceryListViewModel.insert(firstItem); //insert first item into Grocery List DB
                                }
                            }
                            return;

                        } catch (IngredientNotFoundException ne){
                            progressDialog.dismiss();

                            createDialog("Sorry, but we could not find the ingredient you're looking for.");

                            Log.e("INGREDIENT NOT FOUND", ne.toString());
                            progressDialog.dismiss();
                        }
                        catch (Exception e) {
                            Log.e("ASYNCTASK ERROR", e.toString());
                            createDialog("Sorry, but there was an issue connecting to the API. Please try again when you have an internet connection.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API ERROR response", error.toString());
                        progressDialog.dismiss();
                        createDialog("Sorry, but there was an error in processing your ingredient search. Please try again.");
                    }
                });

        requestQueue.add(objectReq);
        objectReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    public void SeqAPIFetchAndInsert(final String ingredient){

        String foodApiURLProper = foodApiDomainNPath + "?ingr=" + ingredient + "&app_id=" + appID + "&app_key=" + appKey;
        foodApiURLProper = foodApiURLProper.replaceAll("%", " ");

        Log.e("API URL", foodApiURLProper);

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

                            //No items returned from API means we must fabricate our own
                            if (hintsIngredientsArray.length() == 0){
                                String foodId = "food_" + UUID.randomUUID().toString();
                                UserGroceryListItem item = new UserGroceryListItem(foodId, ingredient);
                                item.setNotes("From Your Week Meal Plan");
                                groceryListViewModel.insert(item);
                            }

                            Gson gson = new GsonBuilder().create();
                            UserGroceryListItem firstItem = null;

                            for (int i = 0; i < hintsIngredientsArray.length(); i++){
                                JSONObject ingredientParsed = hintsIngredientsArray.getJSONObject(i); //Gets the element returned at i position in the "hints" array
                                String stringRepOfFoodObj = ingredientParsed.getString("food");
                                Food f = gson.fromJson(stringRepOfFoodObj, Food.class);
                                f.label = WordUtils.capitalizeFully(f.getLabel());
                                groceryListViewModel.insert(f); //insert Food into DB
                                if (i == 0){
                                    firstItem = new UserGroceryListItem(f.foodId, f.label);
                                    firstItem.setNotes("From Your Weekly Meal Plan");
                                    groceryListViewModel.insert(firstItem); //insert first item into Grocery List DB
                                }
                            }
                            return;
                        }
                        catch (Exception e) {
                            Log.e("ASYNCTASK ERROR", e.toString());
                            createDialog("Sorry, but there was an issue connecting to the API. Please try again when you have an internet connection.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API ERROR response", error.toString());
                        createDialog("Sorry, but there was an error in processing your ingredient search. Please try again.");
                    }
                });

        requestQueue.add(objectReq);
        objectReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    public void createDialog(String errorMessage){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroceryListView.this);
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.error_dialog_grocery, null, false);

        alertDialog.setView(customView);
        final AlertDialog showAD = alertDialog.show();

        TextView text = (TextView) customView.findViewById(R.id.dialog_message);
        text.setText(errorMessage);
        Button closeButton = (Button) customView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAD.dismiss();
            }
        });
    }

    private class GetFoodByLabelAsyncTask extends AsyncTask<String, Void, Food>{

        @Override
        protected Food doInBackground(String... strings) {
            int count = strings == null ? 0 : strings.length;
            List<Food> foodFromDB = null;
            for (int i = 0; i < count; i++){
                foodFromDB = groceryListViewModel.getFoodByLabel(strings[i]);
            }

            if (foodFromDB != null && foodFromDB.size() != 0 && foodFromDB.get(0) != null){
                return foodFromDB.get(0);
            }
            return null;
        }
    }

    private class GetGroceryItemByLabelAsyncTask extends AsyncTask<String, Void, List<UserGroceryListItem>>{

        @Override
        protected List<UserGroceryListItem> doInBackground(String... strings) {
            int count = strings == null ? 0 : strings.length;
            List<UserGroceryListItem> ingredientFromDB = null;
            for (int i = 0; i < count; i++){
                ingredientFromDB = groceryListViewModel.getItemInGroceryList(strings[i]);
            }
            return ingredientFromDB;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) { return networkInfo.isConnected();}
        return false;
    }


}
