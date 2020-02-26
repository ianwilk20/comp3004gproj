package fooderie.recipeBrowser.models;

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
    public String url;

    public String label;
    public String image;
    @Ignore public ArrayList<String> dietLabels;
    @Ignore public ArrayList<String> healthLabels;
    @Ignore public ArrayList<String> theIngredients = new ArrayList<String>();
    @Ignore public TotalNutrients totalNutrients;

    //Getters
    @NonNull
    public String getUrl()   {return url;}
    public String getLabel() {return label;}
    public String getImage() {return image;}
    @Ignore public ArrayList<String> getDietLabels() {return dietLabels;}
    @Ignore public ArrayList<String> getHealthLabels() {return healthLabels;}

    //Setters
    public void setUrl(@NonNull String id)  {this.url = id;}
    public void setLabel(String l)          {this.label = l;}
    public void setImage(String i)          {this.image = i;}
    @Ignore public void setDietLabels(ArrayList<String> items) {this.dietLabels = items;}
    @Ignore public void setHealthLabels(ArrayList<String> items){this.healthLabels = items;}

    public Recipe(){
    }

    @Ignore public Recipe(String l, String u, String im, ArrayList<String> dl, ArrayList<String> hl, TotalNutrients tn){
        label = l;
        url = u;
        image = im;
        dietLabels = dl;
        healthLabels = hl;
        totalNutrients = tn;
    }
}
