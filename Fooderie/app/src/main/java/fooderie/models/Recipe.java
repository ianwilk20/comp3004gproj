package fooderie.models;

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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="recipe_id")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Recipe(){
    }

    @Ignore public String label;
    @Ignore public String url;
    @Ignore public String image;
    @Ignore public ArrayList<String> dietLabels;
    @Ignore public ArrayList<String> healthLabels;
    @Ignore public ArrayList<String> ingredientLines;
    @Ignore public ArrayList<RecipeIngredient> ingredients;
    @Ignore public TotalNutrients totalNutrients;

    @Ignore public Recipe(String l, String u, String im, ArrayList<String> dl, ArrayList<String> hl, ArrayList<String> il, ArrayList<RecipeIngredient> i, TotalNutrients tn){
        label =l;
        url = u;
        image = im;
        dietLabels = dl;
        healthLabels = hl;
        ingredientLines = il;
        ingredients = i;
        totalNutrients = tn;
    }
}
