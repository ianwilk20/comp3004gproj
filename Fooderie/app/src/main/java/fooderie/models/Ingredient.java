package fooderie.models;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import fooderie.groceryList.GroceryList;

public class Ingredient {
    public String ingredientName;
    public Food ingredientParsed;
    public ArrayList<Hints> hintsParsed;
    public String[] nextPage;

    public Ingredient(){
        ingredientName = "";
        ingredientParsed = new Food();
        hintsParsed = new ArrayList<Hints>();
        nextPage = new String[10];
    }

    public Ingredient(String name, Food ingrParsed, ArrayList<Hints> hints, String[] page){
        ingredientName = name;
        ingredientParsed = ingrParsed;
        hintsParsed = hints;
        nextPage = page;
    }

}
