package fooderie.recipeBrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.util.List;
import java.util.concurrent.ExecutionException;
import fooderie.recipeBrowser.models.Recipe;
import fooderie.recipeBrowser.viewModels.RBViewModel;
import android.widget.Toast;
import android.app.ProgressDialog;

public class rbSearch extends AppCompatActivity {
    private RBViewModel viewModel;

    ProgressDialog dialog;
    SearchView rbSearchView;
    ListView resultsListView;
    ListView favListView;
    RequestQueue rbRequestQueue;
    ArrayAdapter<String> resultsArrAdapt;
    ArrayAdapter<String> favArrAdapt;
    ArrayList<Recipe> recipeFavList;
    ArrayList<String> stringFavList = new ArrayList<String>();
    ArrayList<String> stringResultsList = new ArrayList<String>();
    ArrayList<Recipe> recipeResultsList = new ArrayList<Recipe>();
    String preferencesUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb_search);

        viewModel = ViewModelProviders.of(this).get(RBViewModel.class);

        //Get String from Meal Plan or MainActivity
        Intent intent = getIntent();
        String fromPlan = (String)intent.getSerializableExtra("FROMPLAN");

        rbSearchView = findViewById(R.id.rbSearchView);
        resultsListView = findViewById(R.id.rbListView);
        favListView = findViewById(R.id.favListView);

        rbRequestQueue = Volley.newRequestQueue(this);
        dialog = new ProgressDialog(rbSearch.this);

        //put search results into list
        resultsArrAdapt = new ArrayAdapter(rbSearch.this, android.R.layout.simple_list_item_1, stringResultsList);
        resultsListView.setAdapter(resultsArrAdapt);

        //put fav list into list
        favArrAdapt = new ArrayAdapter(rbSearch.this, android.R.layout.simple_list_item_1, stringFavList);
        favListView.setAdapter(favArrAdapt);
        List<Recipe> tempFavList = FetchAllFavs();
        if((tempFavList != null)&&(tempFavList.size() != 0)) {
            recipeFavList = new ArrayList<Recipe>(tempFavList);
            for (Recipe r : recipeFavList) {
                stringFavList.add(r.label);
            }
        }
        else {
            recipeFavList = new ArrayList<Recipe>();
        }
        favArrAdapt.notifyDataSetChanged();

        //If an option switch is checked, add its query parameter
        //Otherwise, remove its query parameter
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.ALCOHOL_FREE_SWITCH, false)  ? preferencesUrl + "&health=alcohol-free"   : preferencesUrl.replace("&health=alcohol-free", "");
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.LOW_CARB_SWITCH, false)      ? preferencesUrl + "&diet=low-carb"         : preferencesUrl.replace("&diet=low-carb", "");
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.LOW_FAT_SWITCH, false)       ? preferencesUrl + "&diet=low-fat"          : preferencesUrl.replace("&diet=low-fat", "");
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.PEANUT_FREE_SWITCH, false)   ? preferencesUrl + "&health=peanut-free"    : preferencesUrl.replace("&health=peanut-free", "");
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.TREE_NUT_FREE_SWITCH, false) ? preferencesUrl + "&health=tree-nut-free"  : preferencesUrl.replace("&health=tree-nut-free", "");
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.VEGAN_SWITCH, false)         ? preferencesUrl + "&health=vegan"          : preferencesUrl.replace("&health=vegan", "");
        preferencesUrl = sharedPref.getBoolean(OptionsActivity.VEGETARIAN_SWITCH, false)    ? preferencesUrl + "&health=vegetarian"     : preferencesUrl.replace("&health=vegetarian", "");

        rbSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //make Query
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>= 1 && query != "null"){
                    stringResultsList.clear();
                    resultsListView.setVisibility(View.VISIBLE);
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

        //Get Item Selected and redirect to rbDisplay activity from results list
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selected = null;
                for (int i = 0; i < recipeResultsList.size(); ++i){
                    if(recipeResultsList.get(i).label.equals(stringResultsList.get(position))){
                        selected = recipeResultsList.get(i);
                    }
                }
                if (selected != null) {
                    displaySelected(selected, fromPlan);
                }
            }
        });

        //Get Item Selected and redirect to rbDisplay activity from fav list
        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selected = null;
                for (int i = 0; i < recipeFavList.size(); ++i){
                    if(recipeFavList.get(i).label.equals(stringFavList.get(position))){
                        selected = recipeFavList.get(i);
                    }
                }
                if (selected != null) {
                    displaySelected(selected, fromPlan);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        stringFavList.clear();

        favListView = findViewById(R.id.favListView);

        favArrAdapt = new ArrayAdapter(rbSearch.this, android.R.layout.simple_list_item_1, stringFavList);
        favListView.setAdapter(favArrAdapt);
        List<Recipe> allFavsList = FetchAllFavs();
        if((allFavsList != null)&&(allFavsList.size() != 0)) {
            recipeFavList = new ArrayList<Recipe>(allFavsList);
            for (Recipe r : recipeFavList) {
                stringFavList.add(r.label);
            }
        }
        else {
            recipeFavList = new ArrayList<Recipe>();
        }
        favArrAdapt.notifyDataSetChanged();
    }

    //make Query continued
    public void jsonFetch(final String query){
        final String NO_RESULTS = "No Results";
        String appKey = "e58bb1dfb29eece35b4b33b46a084d56";
        String appID = "63cfb724";
        String rbApiUrl = "https://api.edamam.com/search?q=" + query + "&app_id=" + appID +"&app_key=" + appKey + "&from=0&to=50" + preferencesUrl;

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

                            //no response
                            if (rbResponse.length() <= 0) {
                                dialog.dismiss();
                                Toast.makeText(rbSearch.this, NO_RESULTS, Toast.LENGTH_SHORT).show();
                                throw new Exception(NO_RESULTS);
                            }

                            for (int i = 0; i < rbResponse.length(); ++i) {
                                JSONObject object = rbResponse.getJSONObject(i);

                                //put response into recipe class
                                String stringifiedRecipe = object.getString("recipe");
                                Gson gson = new GsonBuilder().create();
                                Recipe recipe = gson.fromJson(stringifiedRecipe, Recipe.class);

                                //put response ingredients into theIngredients arraylist
                                JSONObject recipeObj = object.getJSONObject("recipe");
                                JSONArray ingredientsParsed = recipeObj.getJSONArray("ingredients");
                                for (int k = 0; k < ingredientsParsed.length(); k++){
                                    JSONObject ingredientObj = ingredientsParsed.getJSONObject(k);
                                    recipe.theIngredients.add(ingredientObj.getString("text"));
                                }

                                //add recipes to recipe list
                                //and their label to list array
                                if(recipe.url.contains("food52")||recipe.url.contains("foodnetwork")||recipe.url.contains("seriouseats")) {
                                    recipeResultsList.add(recipe);
                                    stringResultsList.add(recipe.label);
                                }
                            }

                            //no usable response
                            if(recipeResultsList.size() <= 0){
                                dialog.dismiss();
                                Toast.makeText(rbSearch.this, NO_RESULTS, Toast.LENGTH_SHORT).show();
                                throw new Exception(NO_RESULTS);
                            }

                            resultsArrAdapt.notifyDataSetChanged();
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

    //Redirect to rbDisplay activity
    //and pass selected recipe
    //and the Meal Plan or Main Activity in fromPlan
    public void displaySelected(Recipe selected, String fromPlan){
        Intent rbIntent = new Intent(this, rbDisplay.class);
        rbIntent.putExtra("RECIPE", selected);
        rbIntent.putExtra("FROMPLAN", fromPlan);
        startActivityForResult(rbIntent, 1);
    }

    //If data is returned from rbDisplay
    //Redirect back to Meal Plan
    //and pass selected recipe
    //and pass recipe ID
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
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

    public List<Recipe> FetchAllFavs(){
        GetAllFavsAsyncTask task = new GetAllFavsAsyncTask();
        task.execute();

        List<Recipe> recipe = null;
        try{
            recipe = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private class GetAllFavsAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {
        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            List<Recipe> recipe = null;
            recipe = viewModel.getAllFavs();
            return recipe;
        }
    }
}
