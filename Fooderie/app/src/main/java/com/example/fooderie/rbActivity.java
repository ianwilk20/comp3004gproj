package com.example.fooderie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fooderie.models.Recipe;
import fooderie.models.Tag;


public class rbActivity extends AppCompatActivity {

    SearchView rbSearchView;
    ListView rbListView;
    RequestQueue rbRequestQueue;
    ArrayAdapter<String> rbArrAdapt;
    ArrayList<String> rbResults = new ArrayList<String>();
    ArrayList<Recipe> rbRecipeArr = new ArrayList<Recipe>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb);
        Log.e("IN ON CREATE", "we're in on create");

        rbSearchView = findViewById(R.id.rbSearchView);
        rbListView = findViewById(R.id.rbListView);
        rbRequestQueue = Volley.newRequestQueue(this);
        rbArrAdapt = new ArrayAdapter(rbActivity.this, android.R.layout.simple_list_item_1, rbResults);
        rbListView.setAdapter(rbArrAdapt);

        rbSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>= 1 && query != "null"){
                    rbListView.setVisibility(View.VISIBLE);
                    jsonFetch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Item Selected
        rbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("SELECTED", rbResults.get(position));
                Recipe selected = null;
                for (int i = 0; i < rbRecipeArr.size(); ++i){
                    if(rbRecipeArr.get(i).label.equals(rbResults.get(position))){
                        selected = rbRecipeArr.get(i);
                        Log.e("SELECTED URL", selected.url);
                    }
                }
                if (selected != null) {
                    rbListView.setVisibility(View.GONE); //TEMPORARY
                    openSelected(selected);
                    rbResults.clear();
                }
            }
        });

    }

    public void jsonFetch(final String recipe){
        //String queryParams;
        String appKey = "e58bb1dfb29eece35b4b33b46a084d56";
        String appID = "63cfb724";
        String rbApiUrl = "https://api.edamam.com/search?q=" + recipe + "&app_id=" + appID +"&app_key=" + appKey;// + "&from=0&to=3&calories=591-722&health=alcohol-free"

        //Log.e("API URL", foodApiURL);

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
                                throw new Exception("No Results");
                            }


                            for (int i = 0; i < rbResponse.length(); ++i) {
                                //parse object
                                JSONObject recipeObj = rbResponse.getJSONObject(i);
                                //JSONObject recipeParsed = recipeObj.getJSONObject("recipe");

                                String stringifiedRecipe = recipeObj.getString("recipe");
                                Gson gson = new GsonBuilder().create();

                                Recipe recipe = gson.fromJson(stringifiedRecipe, Recipe.class);

                                rbRecipeArr.add(recipe);
                                rbResults.add(recipe.label);
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

    public void openSelected(Recipe selected){
        Intent rbIntent = new Intent(this, rbSelected.class);
        rbIntent.putExtra("RECIPE", selected);
        startActivity(rbIntent);
//        rbSelected.newObject=item;
//        Intent intent=new Intent(OldActivity.this, NewActivity.class);
//        startActivity(intent);
    }
}
