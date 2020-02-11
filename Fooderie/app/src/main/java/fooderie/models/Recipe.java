package fooderie.models;

import android.graphics.Bitmap;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("serial")
@Entity (tableName = "table_Recipe",
         indices = {@Index("recipe_id")})
public class Recipe implements Serializable {
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

//    @Ignore public Recipe(Parcel source){
//        label = source.readString();
//        url = source.readString();
//        dietLabels = source.createStringArrayList();
//        healthLabels = source.createStringArrayList();
//        ingredientLines = source.createStringArrayList();
//        ingredients = source.createTypedArrayList();
//    }


}
