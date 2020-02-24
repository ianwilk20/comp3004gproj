package fooderie.recipeBrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.fooderie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import fooderie.CookingAssistant.views.Views.CookingAssistantPreview;
import fooderie.models.Recipe;
import fooderie.models.Tag;

public class rbSelected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb_selected);

        //Get selected recipe and units from rbActivity
        //Get String from Meal Plan or MainActivity
        Intent intent = getIntent();
        Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");
        String fromPlan = (String)intent.getSerializableExtra("FROMPLAN");
        String units = (String)intent.getSerializableExtra("UNITS");
        if(units.equals("Metric")){
            units = "g";
        }
        if(units.equals("Imperial")){
            units = "oz";
        }

        //Click Listener for Add button
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //add recipe to db
                goBackToPlan(selected);
            }
        });

        //If directed to this activity from Meal Plan
        //Make Add button visible
        if(fromPlan.equals("yes")){
            addButton.setVisibility(View.VISIBLE);
        }

        //Click Listener for website button
        Button websiteButton = findViewById(R.id.website);
        websiteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToWebsite(selected);
            }
        });

        //Click Listener for cooking steps button
        Button stepsButton = findViewById(R.id.steps);
        stepsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToSteps(selected);
            }
        });

        //Recipe title
        TextView recipeLabel = findViewById(R.id.textView);
        recipeLabel.setText(selected.label);

        //Add every image and their corresponding label
        ArrayList<Tag> dietTags = new ArrayList<>();
        ArrayList<Tag> healthTags = new ArrayList<>();

        ImageView low_fat = findViewById(R.id.low_fat);
        dietTags.add(new Tag("Low-Fat", low_fat));
        ImageView low_carb = findViewById(R.id.low_carb);
        dietTags.add(new Tag("Low-Carb", low_carb));

        ImageView vegan = findViewById(R.id.vegan);
        healthTags.add(new Tag("Vegan", vegan));
        ImageView vegetarian = findViewById(R.id.vegetarian);
        healthTags.add(new Tag("Vegetarian", vegetarian));
        ImageView peanut_free = findViewById(R.id.peanut_free);
        healthTags.add(new Tag("Peanut-Free", peanut_free));
        ImageView tree_nut_free = findViewById(R.id.tree_nut_free);
        healthTags.add(new Tag("Tree-Nut-Free", tree_nut_free));
        ImageView alcohol_free = findViewById(R.id.alcohol_free);
        healthTags.add(new Tag("Alcohol-Free", alcohol_free));

        //If the recipe does not have those labels
        //remove the corresponding image
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

        //Image
        ImageView recipeImage = findViewById(R.id.recipeImage);
        Picasso.get().load(selected.image).into(recipeImage);

        //Ingredients list
        ListView ingredientsView = findViewById(R.id.ingredientsView);
        ArrayAdapter<String> rbArrAdapt = new ArrayAdapter(rbSelected.this, android.R.layout.simple_list_item_1, selected.ingredientLines);
        ingredientsView.setAdapter(rbArrAdapt);

        //Nutritional Information
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
            selected.totalNutrients.FAT.setUnits(units);
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
            selected.totalNutrients.CHOCDF.setUnits(units);
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
            selected.totalNutrients.FIBTG.setUnits(units);
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
            selected.totalNutrients.SUGAR.setUnits(units);
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
            selected.totalNutrients.PROCNT.setUnits(units);
            protein.setText(selected.totalNutrients.PROCNT.round());
            proteinUnit.setText(selected.totalNutrients.PROCNT.unit);
        }
        else{
            protein.setText("Unknown");
            proteinUnit.setText("");
        }
    }

    //Redirect to rbWebsite activity
    //and pass selected recipe
    public void goToWebsite(Recipe selected){
        Intent rbIntent = new Intent(this, rbWebsite.class);
        rbIntent.putExtra("RECIPE", selected);
        startActivity(rbIntent);
    }

    //Redirect to CookingAssistantViewer activity
    //and pass selected recipe
    public void goToSteps(Recipe selected){
        Intent rbIntent = new Intent(this, CookingAssistantPreview.class);
        rbIntent.putExtra("RECIPE", selected);
        startActivity(rbIntent);
    }

    //Redirect back to Meal Plan
    //and pass selected recipe
    //and pass recipe ID
    public void goBackToPlan(Recipe selected){
        //go back twice
        Intent rbIntent = new Intent();
        rbIntent.putExtra("RECIPE", selected);
        rbIntent.putExtra("RECIPEID", selected.url);
        setResult(RESULT_OK, rbIntent);
        finish();
    }
}
