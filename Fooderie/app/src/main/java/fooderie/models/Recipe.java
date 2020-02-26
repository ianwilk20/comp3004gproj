package fooderie.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
@Entity (tableName = "table_Recipe",
         indices = {@Index("recipe_id")})
public class Recipe implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="recipe_id")
    // TODO: recipe_id MUST CONTAIN URL, FIX ONCE RECIPES ARE IN DB
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Recipe(){
    }

    @Ignore public String label;
    @Ignore public String url;
    @Ignore public String image;   //Recipe picture
    @Ignore public ArrayList<String> dietLabels;
    @Ignore public ArrayList<String> healthLabels;
    @Ignore public ArrayList<String> ingredientLines;   //List of ingridents as strings
    @Ignore public ArrayList<RecipeIngredient> ingredients; //How much and name of ingridents
    @Ignore public TotalNutrients totalNutrients;

    @Ignore public Recipe(String l, String u, String im, ArrayList<String> dl, ArrayList<String> hl, ArrayList<String> il, ArrayList<RecipeIngredient> i, TotalNutrients tn){
        label = l;
        url = u;
        image = im;
        dietLabels = dl;
        healthLabels = hl;
        ingredientLines = il;
        ingredients = i;
        totalNutrients = tn;
    }
}
