package fooderie.recipeBrowser.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fooderie.options.OptionsActivity;
import com.example.fooderie.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import fooderie.CookingAssistant.views.CookingAssistantPreview;
import fooderie.recipeBrowser.models.Nutrient;
import fooderie.recipeBrowser.models.Recipe;
import fooderie.recipeBrowser.models.Tag;
import fooderie.recipeBrowser.viewModels.RBViewModel;

public class rbDisplay extends AppCompatActivity {
    private RBViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb_display);

        viewModel = ViewModelProviders.of(this).get(RBViewModel.class);

        //Get selected recipe from rbSearch
        //Get String from Meal Plan or MainActivity
        Intent intent = getIntent();
        Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");
        String fromPlan = (String)intent.getSerializableExtra("FROMPLAN");

        //Click Listener for Add button
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBackToPlan(selected);
            }
        });

        //If directed to this activity from Meal Plan
        //Make Add button visible
        if(fromPlan != null && fromPlan.equals("yes")){
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
        setTags(selected);

        //Image
        ImageButton recipeImage = findViewById(R.id.recipeImage);
        Picasso.get().load(selected.image).into(recipeImage);

        //Click Listener for image button
        recipeImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Recipe recipeIfExists = FetchRecipe(selected.url);
                Recipe favIfExists = FetchFav(selected.url);

                //It's in the db
                if(recipeIfExists != null){
                    //And it's not a fav
                    if(favIfExists == null) {
                        //update value - true
                        selected.favorite = true;
                        viewModel.updateRecipeFav(selected.url, selected.favorite);
                        Toast.makeText(rbDisplay.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                    }
                    //And it's a fav
                    if(favIfExists != null) {
                        //update value - false
                        selected.favorite = false;
                        viewModel.updateRecipeFav(selected.url, selected.favorite);
                        Toast.makeText(rbDisplay.this, "Deleted from Favourites", Toast.LENGTH_SHORT).show();
                    }
                }
                //It's not in the db
                else{
                    //insert into db as a fav
                    selected.favorite = true;
                    viewModel.insert(selected);
                    Toast.makeText(rbDisplay.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Ingredients list
        ListView ingredientsView = findViewById(R.id.ingredientsView);
        ArrayAdapter<String> arrAdapt = new ArrayAdapter(rbDisplay.this, android.R.layout.simple_list_item_1, selected.theIngredients);
        ingredientsView.setAdapter(arrAdapt);

        //Nutritional Information
        setNutritionalInfo(selected, selected.totalNutrients.ENERC_KCAL, findViewById(R.id.ENERC_KCAL), findViewById(R.id.ENERC_KCALunit));
        setNutritionalInfo(selected, selected.totalNutrients.FAT, findViewById(R.id.FAT), findViewById(R.id.FATunit));
        setNutritionalInfo(selected, selected.totalNutrients.CHOCDF, findViewById(R.id.CHOCDF), findViewById(R.id.CHOCDFunit));
        setNutritionalInfo(selected, selected.totalNutrients.FIBTG, findViewById(R.id.FIBTG), findViewById(R.id.FIBTGunit));
        setNutritionalInfo(selected, selected.totalNutrients.SUGAR, findViewById(R.id.SUGAR), findViewById(R.id.SUGARunit));
        setNutritionalInfo(selected, selected.totalNutrients.PROCNT, findViewById(R.id.PROCNT), findViewById(R.id.PROCNTunit));
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
        Intent rbIntent = new Intent();
        rbIntent.putExtra("RECIPE", selected);
        rbIntent.putExtra("RECIPEID", selected.url);
        setResult(RESULT_OK, rbIntent);
        finish();
    }

    public void setNutritionalInfo(Recipe selected, Nutrient nutrient, TextView quantityView, TextView unitView){
        //Get option units
        String units;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(OptionsActivity.IMPERIAL_UNITS_CHECK_BOX, false);
        if(switchPref){
            units = "oz";
        }
        else{
            units = "g";
        }

        if(nutrient != null) {
            if(nutrient != selected.totalNutrients.ENERC_KCAL){
                nutrient.setUnits(units);
            }
            quantityView.setText(nutrient.round());
            unitView.setText(nutrient.unit);
        }
        else{
            quantityView.setText("Unknown");
            unitView.setText("");
        }
    }

    public void setTags(Recipe selected){
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
    }

    public Recipe FetchFav(String url){
        GetRecipeFromFavsAsyncTask task = new GetRecipeFromFavsAsyncTask();
        task.execute(url);

        Recipe recipe = null;
        try{
            recipe = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public Recipe FetchRecipe(String url){
        GetRecipeAsyncTask task = new GetRecipeAsyncTask();
        task.execute(url);

        Recipe recipe = null;
        try{
            recipe = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private class GetRecipeFromFavsAsyncTask extends AsyncTask<String, Void, Recipe>{
        @Override
        protected Recipe doInBackground(String... strings) {
            Recipe recipe = null;
            int count = strings == null ? 0 : strings.length;
            for(int i = 0; i < count; i++){
                recipe = viewModel.getFav(strings[i]);
            }
            return recipe;
        }
    }

    private class GetRecipeAsyncTask extends AsyncTask<String, Void, Recipe>{
        @Override
        protected Recipe doInBackground(String... strings) {
            Recipe recipe = null;
            int count = strings == null ? 0 : strings.length;
            for(int i = 0; i < count; i++){
                recipe = viewModel.getRecipe(strings[i]);
            }
            return recipe;
        }
    }
}
