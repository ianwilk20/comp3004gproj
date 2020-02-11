package com.example.fooderie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fooderie.models.Recipe;
import fooderie.models.Tag;

public class rbSelected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb_selected);
        Intent intent = getIntent();
        Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");

        TextView recipeLabel = findViewById(R.id.textView);
        recipeLabel.setText(selected.label);

        ArrayList<Tag> dietTags = new ArrayList<>();
        ArrayList<Tag> healthTags = new ArrayList<>();

        ImageView balanced = findViewById(R.id.balanced);
        dietTags.add(new Tag("Balanced", balanced));
        ImageView high_protein = findViewById(R.id.high_protein);
        dietTags.add(new Tag("High-Protein", high_protein));
        ImageView high_fiber = findViewById(R.id.high_fiber);
        dietTags.add(new Tag("High-Fiber", high_fiber));
        ImageView low_fat = findViewById(R.id.low_fat);
        dietTags.add(new Tag("Low-Fat", low_fat));
        ImageView low_carb = findViewById(R.id.low_carb);
        dietTags.add(new Tag("Low-Carb", low_carb));
        ImageView low_sodium = findViewById(R.id.low_sodium);
        dietTags.add(new Tag("Low-Sodium", low_sodium));

        ImageView vegan = findViewById(R.id.vegan);
        healthTags.add(new Tag("Vegan", vegan));
        ImageView vegetarian = findViewById(R.id.vegetarian);
        healthTags.add(new Tag("Vegetarian", vegetarian));
        ImageView paleo = findViewById(R.id.paleo);
        healthTags.add(new Tag("Paleo", paleo));
        ImageView dairy_free = findViewById(R.id.dairy_free);
        healthTags.add(new Tag("Dairy-Free", dairy_free));
        ImageView gluten_free = findViewById(R.id.gluten_free);
        healthTags.add(new Tag("Gluten-Free", gluten_free));
        ImageView fat_free = findViewById(R.id.fat_free);
        healthTags.add(new Tag("Fat-Free", fat_free));
        ImageView low_sugar = findViewById(R.id.low_sugar);
        healthTags.add(new Tag("Low-Sugar", low_sugar));
        ImageView egg_free = findViewById(R.id.egg_free);
        healthTags.add(new Tag("Egg-Free", egg_free));
        ImageView peanut_free = findViewById(R.id.peanut_free);
        healthTags.add(new Tag("Peanut-Free", peanut_free));
        ImageView tree_nut_free = findViewById(R.id.tree_nut_free);
        healthTags.add(new Tag("Tree-Nut-Free", tree_nut_free));
        ImageView soy_free = findViewById(R.id.soy_free);
        healthTags.add(new Tag("Soy-Free", soy_free));
        ImageView fish_free = findViewById(R.id.fish_free);
        healthTags.add(new Tag("Fish-Free", fish_free));
        ImageView shellfish_free = findViewById(R.id.shellfish_free);
        healthTags.add(new Tag("Shellfish-Free", shellfish_free));
        ImageView alcohol_free = findViewById(R.id.alcohol_free);
        healthTags.add(new Tag("Alcohol-Free", alcohol_free));

        if(selected.dietLabels.size() == 0){
            for(int j = 0; j < dietTags.size(); ++j) {
                dietTags.get(j).image.setVisibility(View.GONE);
            }
        }
        else {
            for (int i = 0; i < selected.dietLabels.size(); ++i) {
                for (int j = 0; j < dietTags.size(); ++j) {
                    if (!selected.dietLabels.contains(dietTags.get(j).id)) {
                        dietTags.get(j).image.setVisibility(View.GONE);
                    }
                }
            }
        }
        if(selected.healthLabels.size() == 0){
            for(int j = 0; j < healthTags.size(); ++j) {
                healthTags.get(j).image.setVisibility(View.GONE);
            }
        }
        else {
            for (int i = 0; i < selected.healthLabels.size(); ++i) {
                for (int j = 0; j < healthTags.size(); ++j) {
                    if (!selected.healthLabels.contains(healthTags.get(j).id)) {
                        healthTags.get(j).image.setVisibility(View.GONE);
                    }
                }
            }
        }

        //PICTURE
        //ImageView recipeImage = findViewById(R.id.recipeImage);
        ListView ingredientsView = findViewById(R.id.ingredientsView);
        ArrayAdapter<String> rbArrAdapt = new ArrayAdapter(rbSelected.this, android.R.layout.simple_list_item_1, selected.ingredientLines);
        ingredientsView.setAdapter(rbArrAdapt);

        //Log.e("Calories", Double.toString(selected.totalNutrients.ENERC_KCAL.quantity));
        TextView calories = findViewById(R.id.ENERC_KCAL);
        TextView caloriesUnit = findViewById(R.id.ENERC_KCALunit);
        if(selected.totalNutrients.ENERC_KCAL != null) {
            calories.setText(selected.totalNutrients.ENERC_KCAL.round());
            caloriesUnit.setText(selected.totalNutrients.ENERC_KCAL.unit);
        }
        else{
            calories.setText("Unknown");
            caloriesUnit.setText("");
        }
        TextView fat = findViewById(R.id.FAT);
        TextView fatUnit = findViewById(R.id.FATunit);
        if(selected.totalNutrients.FAT != null) {
            fat.setText(selected.totalNutrients.FAT.round());
            fatUnit.setText(selected.totalNutrients.FAT.unit);
        }
        else{
            fat.setText("Unknown");
            fatUnit.setText("");
        }
        TextView carbs = findViewById(R.id.CHOCDF);
        TextView carbsUnit = findViewById(R.id.CHOCDFunit);
        if(selected.totalNutrients.CHOCDF != null) {
            carbs.setText(selected.totalNutrients.CHOCDF.round());
            carbsUnit.setText(selected.totalNutrients.CHOCDF.unit);
        }
        else{
            carbs.setText("Unknown");
            carbsUnit.setText("");
        }
        TextView fiber = findViewById(R.id.FIBTG);
        TextView fiberUnit = findViewById(R.id.FIBTGunit);
        if(selected.totalNutrients.FIBTG != null) {
            fiber.setText(selected.totalNutrients.FIBTG.round());
            fiberUnit.setText(selected.totalNutrients.FIBTG.unit);
        }
        else{
            fiber.setText("Unknown");
            fiberUnit.setText("");
        }
        TextView sugar = findViewById(R.id.SUGAR);
        TextView sugarUnit = findViewById(R.id.SUGARunit);
        if(selected.totalNutrients.SUGAR != null) {
            sugar.setText(selected.totalNutrients.SUGAR.round());
            sugarUnit.setText(selected.totalNutrients.SUGAR.unit);
        }
        else{
            sugar.setText("Unknown");
            sugarUnit.setText("");
        }
        TextView protein = findViewById(R.id.PROCNT);
        TextView proteinUnit = findViewById(R.id.PROCNTunit);
        if(selected.totalNutrients.PROCNT != null) {
            protein.setText(selected.totalNutrients.PROCNT.round());
            proteinUnit.setText(selected.totalNutrients.PROCNT.unit);
        }
        else{
            protein.setText("Unknown");
            proteinUnit.setText("");
        }


    }
}
