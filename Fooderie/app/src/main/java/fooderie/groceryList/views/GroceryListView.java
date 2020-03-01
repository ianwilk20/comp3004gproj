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
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    ImageButton saveButton;
    ImageButton optionsButton;
    CustomAdapter groceryListAdapter;
    FloatingActionButton addItem;
    ProgressDialog progressDialog;
    ArrayList<String> grocList = new ArrayList<String>();
    ArrayList<String> gotList = new ArrayList<String>();

    String foodApiURL = "https://api.edamam.com/api/food-database/parser?ingr=basil&app_id=5bfe4f44&app_key=" +
            "d98b79a1c20ab328a1fc73311be5d1ee";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        //Getting tagged items from activity
        groceryList = findViewById(R.id.groceryListDisplay);
        optionsButton = findViewById(R.id.optionsButton);
        requestQueue = Volley.newRequestQueue(this);
        addItem = findViewById(R.id.addGroceryItem);
        saveButton = findViewById(R.id.saveButton);

        groceryListViewModel = ViewModelProviders.of(this).get(GroceryListViewModel.class);
        groceryListViewModel.getUserGroceryList().observe(this, new Observer<List<UserGroceryListItem>>() {
            @Override
            public void onChanged(List<UserGroceryListItem> userGroceryListItems) {
                //update our View later
                groceryListAdapter = new CustomAdapter(userGroceryListItems, getApplicationContext(), groceryListViewModel, GroceryListView.this);
                groceryList.setAdapter(groceryListAdapter);
                groceryListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                //Utility.setListViewHeightBasedOnChildren(groceryList);
                //Toast.makeText(GroceryListView.this, "Grocery List Changed", Toast.LENGTH_SHORT).show();

            }
        });

        groceryListViewModel.getAllFoodItems().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                //Toast.makeText(GroceryListView.this, "Food Table Changed", Toast.LENGTH_SHORT).show();
            }
        });


        LayoutInflater inflater = getLayoutInflater();
        progressDialog = new ProgressDialog(GroceryListView.this);


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
                    body += i.getQuantity().isEmpty() || i.getQuantity() == null ? "" : ", Quantity: " + i.getQuantity();
                    body += i.getNotes().isEmpty() || i.getNotes() == null ? "" : ", Notes: " + i.getNotes();
                    body += i.getDepartment().isEmpty() || i.getDepartment() == null ? "" : ", Department: " + i.getDepartment();
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
    }


    private void addIngredient() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GroceryListView.this);
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
                progressDialog.setMessage("Please, wait while we find your ingredient");
                progressDialog.show();
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

    public boolean FetchIngredientFromDBBoolean(String ingredient){
        //get working with the proper sqlite search
        //LiveData<List<Food>> data = groceryListViewModel.getFoodByLabel(ingredient);
        //List<Food> dataParsed = data.getValue();
//        groceryListViewModel.getFoodByLabel(ingredient.trim()).observe(this, new Observer<List<Food>>() {
//            @Override
//            public void onChanged(List<Food> foods) {
//                 final List<Food> data = foods;
//                 if (data != null && data.size() != 0){
//                     return true;
//                 }
//            }
//        });


        List<Food> data = groceryListViewModel.getAllFoodItems().getValue();
        if (data != null || !data.isEmpty()){
            for(Food f : data){
                Matcher matches = Pattern.compile(Pattern.quote(f.label), Pattern.CASE_INSENSITIVE).matcher(ingredient.trim());
                if (matches.find()){
                    return true;
                }
                //if (f.label.contains(ingredient))
            }
            return false;
        }
        return false;
    }

    public void FetchIngredientFromDB(final String ingredient){
        LiveData<List<Food>> listFromDB = groceryListViewModel.getAllFoodItems();
        List<Food> listParsed = listFromDB.getValue();
        if (listParsed != null){
            for(Food f : listParsed){
                Matcher matches = Pattern.compile(Pattern.quote(f.label), Pattern.CASE_INSENSITIVE).matcher(ingredient.trim());
                if (matches.find()){
                    UserGroceryListItem item = new UserGroceryListItem(f.foodId, f.label);
                    groceryListViewModel.insert(item);
                    return;
                }
            }
        }
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
