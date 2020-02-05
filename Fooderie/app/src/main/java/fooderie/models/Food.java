package fooderie.models;

public class Food {
    public String foodId;
    public String label;
    public Nutrients nutrients;
    public String category;
    public String categoryLabel;

    public Food(){
        foodId = "";
        label = "";
        nutrients = new Nutrients();
        category = "";
        categoryLabel = "";
    }

    public Food(String food_id, String food_label, Nutrients food_nutrients, String food_category, String food_categoryLabel){
        foodId = food_id;
        label = food_label;
        nutrients = food_nutrients;
        category = food_category;
        categoryLabel = food_categoryLabel;
    }
}
