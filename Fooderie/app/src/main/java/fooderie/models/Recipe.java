package fooderie.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity (tableName = "table_Recipe",
         indices = {@Index("recipe_id")})
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="recipe_id")
    private int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Recipe(){
    }

    @Ignore public String label;
    @Ignore public String url;
    @Ignore public ArrayList<String> dietLabels;
    @Ignore public ArrayList<String> healthLabels;
    @Ignore public ArrayList<String> ingredientLines;
    @Ignore public ArrayList<RecipeIngredient> ingredients;

    @Ignore public Recipe(String l, String u, ArrayList<String> dl, ArrayList<String> hl, ArrayList<String> il, ArrayList<RecipeIngredient> i){
        label =l;
        url = u;
        dietLabels = dl;
        healthLabels = hl;
        ingredientLines = il;
        ingredients = i;
    }


}
