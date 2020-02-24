package fooderie.recipeBrowser.models;

import java.io.Serializable;

public class TotalNutrients implements Serializable {

    public Nutrient ENERC_KCAL;
    public Nutrient FAT;
    public Nutrient CHOCDF;
    public Nutrient FIBTG;
    public Nutrient SUGAR;
    public Nutrient PROCNT;

    public TotalNutrients(Nutrient calories, Nutrient fat, Nutrient carbs, Nutrient fiber, Nutrient sugar, Nutrient protein){
        ENERC_KCAL = calories;
        FAT = fat;
        CHOCDF = carbs;
        FIBTG = fiber;
        SUGAR = sugar;
        PROCNT = protein;
    }
}
