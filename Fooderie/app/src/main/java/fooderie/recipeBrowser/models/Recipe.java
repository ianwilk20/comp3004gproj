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
    public String getId() {return url;}

    public String label;
    public String image;
    public Boolean favorite = false;
    public ArrayList<String> dietLabels;
    public ArrayList<String> healthLabels;
    public ArrayList<String> theIngredients = new ArrayList<String>();
    public TotalNutrients totalNutrients;

    //Getters
    @NonNull
    public String getUrl()   {return url;}
    public String getLabel() {return label;}
    public String getImage() {return image;}
    public ArrayList<String> getDietLabels() {return dietLabels;}
    public ArrayList<String> getHealthLabels() {return healthLabels;}
    public ArrayList<String> getTheIngredients() {return theIngredients;}
    public TotalNutrients getTotalNutrients() {return totalNutrients;}

    //Setters
    public void setUrl(@NonNull String id)  {this.url = id;}
    public void setLabel(String l)          {this.label = l;}
    public void setImage(String i)          {this.image = i;}
    public void setDietLabels(ArrayList<String> items) {this.dietLabels = items;}
    public void setHealthLabels(ArrayList<String> items){this.healthLabels = items;}
    public void setTheIngredients(ArrayList<String> items){this.theIngredients = items;}
    public void setTotalNutrients(TotalNutrients total) {this.totalNutrients = total;}

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
