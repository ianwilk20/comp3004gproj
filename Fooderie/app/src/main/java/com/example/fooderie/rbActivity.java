package com.example.fooderie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;


public class rbActivity extends AppCompatActivity {

    SearchView rbSearchView;
    TextView rbResultView;
    RequestQueue rbRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb);

        rbSearchView = findViewById(R.id.rbSearchView);
        rbResultView = findViewById(R.id.rbResultItems);

        rbSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>= 1 && query != "null"){
                    jsonFetch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void jsonFetch(final String recipe){
        String foodApiURL = "https://api.edamam.com/api/food-database/parser?ingr=basil&app_id=5bfe4f44&app_key=" +
                "d98b79a1c20ab328a1fc73311be5d1ee";
        String foodApiDomainNPath = "https://api.edamam.com/api/food-database/parser";
        String queryParams;
        String appKey = "d98b79a1c20ab328a1fc73311be5d1ee";
        String appID = "5bfe4f44";

        String foodApiURLProper = foodApiDomainNPath + "?ingr=" + recipe + "&app_id=" + appID + "&app_key=" + appKey;

        Log.e("API URL", foodApiURL);

        JsonObjectRequest objectReq = new JsonObjectRequest(
                Request.Method.GET,
                foodApiURLProper,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("API SUCCESS response", response.toString());
                        try {
//                            JSONObject responseTextObj = response.getJSONObject("text");
//                            String foundItem = responseTextObj.getString("text");
//                            JSONArray responseParsedObj = response.getJSONArray("parsed");
//                            for (int i = 0; i < responseTextObj.length(); i++) {
//                                JSONObject parsed = responseParsedObj.getJSONObject(i);
//
//                                String food = parsed.getString("food");
                            Toast.makeText(rbActivity.this, "SUCCESS, found: " + recipe, Toast.LENGTH_SHORT).show();
                            rbResultView.append("SUCCESS, found: " + recipe);
                        //}
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
}
