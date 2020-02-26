package fooderie.groceryList.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "table_APIIngredient",
         indices = {@Index("food_id")})
public class Food {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "food_id")
    public String foodId;

    public String label;

    //@TypeConverters(Converters.class)
    //public Nutrients nutrients;
    public String category;
    public String categoryLabel;

    @NonNull
    public String getFoodId(){return foodId;}

    public String getLabel(){return label;}
    public String getCategory(){return category;}
    public String getCategoryLabel(){return categoryLabel;}

    public void setFoodId(@NonNull String foodId){this.foodId = foodId;}

    public void setLabel(String label){this.label = label;}
    public void setCategory(String category){this.category = category;}
    public void setCategoryLabel(String categoryLabel){ this.categoryLabel = categoryLabel;}

    public Food(){

    }

    @Ignore
    public Food(String food_id, String food_label, String food_category, String food_categoryLabel){
        foodId = food_id;
        label = food_label;
        //nutrients = food_nutrients;
        category = food_category;
        categoryLabel = food_categoryLabel;
    }
}
