package fooderie.recipeBrowser.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.fooderie.OptionsActivity;
import com.example.fooderie.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Arrays;
import fooderie.recipeBrowser.models.Recipe;
import android.widget.Toast;
import android.app.ProgressDialog;

public class rbActivity extends AppCompatActivity {

    ProgressDialog dialog;
    SearchView rbSearchView;
    ListView rbListView;
    ListView favListView;
    RequestQueue rbRequestQueue;
    ArrayAdapter<String> rbArrAdapt;
    ArrayList<String> rbResults = new ArrayList<String>();
    ArrayList<Recipe> rbRecipeArr = new ArrayList<Recipe>();
    String preferencesUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb);

        //Get String from Meal Plan or MainActivity
        Intent intent = getIntent();
        String fromPlan = (String)intent.getSerializableExtra("FROMPLAN");

        rbSearchView = findViewById(R.id.rbSearchView);
        rbListView = findViewById(R.id.rbListView);
        favListView = findViewById(R.id.favListView);
        rbRequestQueue = Volley.newRequestQueue(this);
        dialog = new ProgressDialog(rbActivity.this);

        //put search results into list
        rbArrAdapt = new ArrayAdapter(rbActivity.this, android.R.layout.simple_list_item_1, rbResults);
        rbListView.setAdapter(rbArrAdapt);

        //THIS IS ALL TEMPORARY AND NEEDS TO BE DELETED!!!!!
        ArrayList<String> tempList = new ArrayList<String>(Arrays.asList("London", "Tokyo", "New York"));
        ArrayAdapter<String> temp = new ArrayAdapter(rbActivity.this, android.R.layout.simple_list_item_1, tempList);
        favListView.setAdapter(temp);

        //If an option switch is checked, add its query parameter
        //Otherwise, remove its query parameter
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean af_switchPref = sharedPref.getBoolean(OptionsActivity.ALCOHOL_FREE_SWITCH, false);
        if(af_switchPref){
            preferencesUrl += "&health=alcohol-free";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&health=alcohol-free", "");
        }

        Boolean lc_switchPref = sharedPref.getBoolean(OptionsActivity.LOW_CARB_SWITCH, false);
        if (lc_switchPref) {
            preferencesUrl += "&diet=low-carb";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&diet=low-carb", "");
        }

        Boolean lf_switchPref = sharedPref.getBoolean(OptionsActivity.LOW_FAT_SWITCH, false);
        if (lf_switchPref) {
            preferencesUrl += "&diet=low-fat";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&diet=low-fat", "");
        }

        Boolean pf_switchPref = sharedPref.getBoolean(OptionsActivity.PEANUT_FREE_SWITCH, false);
        if (pf_switchPref) {
            preferencesUrl += "&health=peanut-free";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&health=peanut-free", "");
        }

        Boolean tnf_switchPref = sharedPref.getBoolean(OptionsActivity.TREE_NUT_FREE_SWITCH, false);
        if (tnf_switchPref) {
            preferencesUrl += "&health=tree-nut-free";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&health=tree-nut-free", "");
        }

        Boolean vn_switchPref = sharedPref.getBoolean(OptionsActivity.VEGAN_SWITCH, false);
        if (vn_switchPref) {
            preferencesUrl += "&health=vegan";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&health=vegan", "");
        }

        Boolean veg_switchPref = sharedPref.getBoolean(OptionsActivity.VEGETARIAN_SWITCH, false);
        if (veg_switchPref) {
            preferencesUrl += "&health=vegetarian";
        }
        else {
            preferencesUrl = preferencesUrl.replace("&health=vegetarian", "");
        }

        rbSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //make Query
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>= 1 && query != "null"){
                    rbResults.clear();
                    rbListView.setVisibility(View.VISIBLE);
                    favListView.setVisibility(View.GONE);
                    jsonFetch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Get Item Selected and redirect to rbSelected activity from results list
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
                    openSelected(selected, fromPlan);
                }
            }
        });

        //Get Item Selected and redirect to rbSelected activity from fav list
        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selected = null;
                //THIS NEEDS TO BE CHANGED FOR FAVLIST
//                for (int i = 0; i < rbRecipeArr.size(); ++i){
//                    if(rbRecipeArr.get(i).label.equals(rbResults.get(position))){
//                        selected = rbRecipeArr.get(i);
//                    }
//                }
                if (selected != null) {
                    openSelected(selected, fromPlan);
                }
            }
        });
    }

    //make Query continued
    public void jsonFetch(final String recipe){
        String appKey = "e58bb1dfb29eece35b4b33b46a084d56";
        String appID = "63cfb724";
        String rbApiUrl = "https://api.edamam.com/search?q=" + recipe + "&app_id=" + appID +"&app_key=" + appKey + "&from=0&to=30" + preferencesUrl;

        dialog.setMessage("Please, wait while we find recipes");
        dialog.show();

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
                                dialog.dismiss();
                                Toast.makeText(rbActivity.this, "No Results", Toast.LENGTH_SHORT).show();
                                throw new Exception("No Results");
                            }

                            for (int i = 0; i < rbResponse.length(); ++i) {
                                JSONObject recipeObj = rbResponse.getJSONObject(i);
                                String stringifiedRecipe = recipeObj.getString("recipe");
                                JSONObject actualRecipeObj = recipeObj.getJSONObject("recipe");
                                JSONArray ingredientsParsed = actualRecipeObj.getJSONArray("ingredients");
                                Gson gson = new GsonBuilder().create();
                                Recipe recipe = gson.fromJson(stringifiedRecipe, Recipe.class);

                                for (int k = 0; k < ingredientsParsed.length(); k++){
                                    JSONObject ingredientObj = ingredientsParsed.getJSONObject(k);
                                    recipe.theIngredients.add(ingredientObj.getString("text"));
                                }

                                //add recipes to recipe list
                                //and their label to list array
                                if(recipe.url.contains("food52")||recipe.url.contains("foodnetwork")||recipe.url.contains("seriouseats")) {
                                    rbRecipeArr.add(recipe);
                                    rbResults.add(recipe.label);
                                }
                            }

                            if(rbRecipeArr.size() <= 0){
                                dialog.dismiss();
                                Toast.makeText(rbActivity.this, "No Results", Toast.LENGTH_SHORT).show();
                                throw new Exception("No Results");
                            }

                            rbArrAdapt.notifyDataSetChanged();
                            dialog.dismiss();
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

    //Redirect to rbSelected activity
    //and pass selected recipe
    //and the Meal Plan or Main Activity in fromPlan
    public void openSelected(Recipe selected, String fromPlan){
        Intent rbIntent = new Intent(this, rbSelected.class);
        rbIntent.putExtra("RECIPE", selected);
        rbIntent.putExtra("FROMPLAN", fromPlan);
        startActivityForResult(rbIntent, 1);
    }

    //If data is returned from rbSelected
    //Redirect back to Meal Plan
    //and pass selected recipe
    //and pass recipe ID
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                Intent intent = getIntent();
                Recipe selected = (Recipe) data.getSerializableExtra("RECIPE");

                Intent rbIntent = new Intent();
                rbIntent.putExtra("RECIPE", selected);
                rbIntent.putExtra("RECIPEID", selected.url);
                setResult(RESULT_OK, rbIntent);
                finish();
            }
            catch(Exception e){
                Log.e("No Error", "Recipe not returned");
            }
        }
    }
}
