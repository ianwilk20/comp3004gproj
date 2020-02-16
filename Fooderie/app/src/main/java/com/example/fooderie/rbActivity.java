package com.example.fooderie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import android.view.View;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import java.util.ArrayList;
import fooderie.models.Recipe;
import android.widget.Toast;
import android.widget.ToggleButton;

public class rbActivity extends AppCompatActivity {

    SearchView rbSearchView;
    ListView rbListView;
    RequestQueue rbRequestQueue;
    ArrayAdapter<String> rbArrAdapt;
    ArrayList<String> rbResults = new ArrayList<String>();
    ArrayList<Recipe> rbRecipeArr = new ArrayList<Recipe>();
    String preferencesUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb);


        rbSearchView = findViewById(R.id.rbSearchView);
        rbListView = findViewById(R.id.rbListView);
        rbRequestQueue = Volley.newRequestQueue(this);

        //put search results into list
        rbArrAdapt = new ArrayAdapter(rbActivity.this, android.R.layout.simple_list_item_1, rbResults);
        rbListView.setAdapter(rbArrAdapt);

        rbSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //make Query
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>= 1 && query != "null"){
                    rbResults.clear();
                    rbListView.setVisibility(View.VISIBLE);
                    jsonFetch(query);
                }
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                rbResults.clear();
                rbListView.setVisibility(View.GONE);
                return false;
            }
        });

        //Get Item Selected and redirect to rbSelected activity
        rbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selected = null;
                for (int i = 0; i < rbRecipeArr.size(); ++i){
                    if(rbRecipeArr.get(i).label.equals(rbResults.get(position))){
                        selected = rbRecipeArr.get(i);
                    }
                }
                if (selected != null) {
                    rbListView.setVisibility(View.GONE);
                    openSelected(selected);
                    rbResults.clear();
                }
            }
        });

        //Click Listener for preferences button
        Button preferencesButton = findViewById(R.id.preferences);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPreferences();
            }
        });
    }

    //make Query continued
    public void jsonFetch(final String recipe){
        String appKey = "e58bb1dfb29eece35b4b33b46a084d56";
        String appID = "63cfb724";
        String rbApiUrl = "https://api.edamam.com/search?q=" + recipe + "&app_id=" + appID +"&app_key=" + appKey + "&from=0&to=30" + preferencesUrl;

        JsonObjectRequest objectReq = new JsonObjectRequest(
                Request.Method.GET,
                rbApiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("API SUCCESS response", response.toString());
                        try {
                            JSONArray rbResponse = response.getJSONArray("hits");
                            if (rbResponse.length() <= 0) {
                                Toast.makeText(rbActivity.this, "No Results", Toast.LENGTH_SHORT).show();
                                throw new Exception("No Results");
                            }

                            for (int i = 0; i < rbResponse.length(); ++i) {
                                JSONObject recipeObj = rbResponse.getJSONObject(i);
                                String stringifiedRecipe = recipeObj.getString("recipe");
                                Gson gson = new GsonBuilder().create();
                                Recipe recipe = gson.fromJson(stringifiedRecipe, Recipe.class);

                                //add recipes to recipe list
                                //and their label to list array
                                if(recipe.url.contains("food52")||recipe.url.contains("foodnetwork")||recipe.url.contains("seriouseats")) {
                                    rbRecipeArr.add(recipe);
                                    rbResults.add(recipe.label);
                                }
                            }

                            if(rbRecipeArr.size() <= 0){
                                Toast.makeText(rbActivity.this, "No Results", Toast.LENGTH_SHORT).show();
                                throw new Exception("No Results");
                            }

                            rbArrAdapt.notifyDataSetChanged();
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
                }
        );
        rbRequestQueue.add(objectReq);
    }

    //Show set_preferences xml in an Alert Dialog
    private void setPreferences() {
        preferencesUrl = "";

        //Set Dialog Title
        AlertDialog.Builder builder = new AlertDialog.Builder(rbActivity.this);
        builder.setTitle("Preferences");

        //Set View layout to set_preferences.xml
        View v = LayoutInflater.from(rbActivity.this).inflate(R.layout.set_preferences, null, false);
        builder.setView(v);

        //If a toggle button is checked, add its query parameter
        //Otherwise, remove its query parameter
        ToggleButton aftoggle = v.findViewById(R.id.alcohol_free_toggle_button);
        aftoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&health=alcohol-free";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&health=alcohol-free", "");
                }
            }
        });

        ToggleButton lctoggle = v.findViewById(R.id.low_carb_toggle_button);
        lctoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&diet=low-carb";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&diet=low-carb", "");
                }
            }
        });

        ToggleButton lftoggle = v.findViewById(R.id.low_fat_toggle_button);
        lftoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&diet=low-fat";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&diet=low-fat", "");
                }
            }
        });

        ToggleButton pftoggle = v.findViewById(R.id.peanut_free_toggle_button);
        pftoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&health=peanut-free";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&health=peanut-free", "");
                }
            }
        });

        ToggleButton tnftoggle = v.findViewById(R.id.tree_nut_free_toggle_button);
        tnftoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&health=tree-nut-free";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&health=tree-nut-free", "");
                }
            }
        });

        ToggleButton vegantoggle = v.findViewById(R.id.vegan_toggle_button);
        vegantoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&health=vegan";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&health=vegan", "");
                }
            }
        });

        ToggleButton vegtoggle = v.findViewById(R.id.vegetarian_toggle_button);
        vegtoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesUrl += "&health=vegetarian";
                }
                else {
                    preferencesUrl = preferencesUrl.replace("&health=vegetarian", "");
                }
            }
        });

        //Add button to close dialog
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    //Redirect to rbSelected activity
    //and pass selected recipe
    public void openSelected(Recipe selected){
        Intent rbIntent = new Intent(this, rbSelected.class);
        rbIntent.putExtra("RECIPE", selected);
        startActivity(rbIntent);
    }
}
