package fooderie.CookingAssistant.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fooderie.MainActivity;
import fooderie.CookingAssistant.views.CookingAssistantViewer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooderie.R;

import java.util.ArrayList;

import fooderie.models.Recipe;
import fooderie.models.RecipeIngredient;


public class CookingAssistantPreview extends AppCompatActivity
{
    private Button startButton;
    private TextView ingridentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Get selected recipe from rbActivity
        Intent intent = getIntent();
        Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_pre);
        startButton = findViewById(R.id.btnStart);
        ingridentText = findViewById(R.id.txtIngridents);

        /*
        FILL IN IMAGE PREVIEW HERE
         */

        ArrayList<RecipeIngredient> ingredients = selected.ingredients;
        String steps = "";
        int count = 1;
        for (RecipeIngredient ingredient : ingredients)
        {
            steps += count + ". " + ingredient.weight + " - " + ingredient.text + "\n";
        }
        ingridentText.setText(steps);   //Set text to steps

        final AppCompatActivity cAssistThis = this;
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToSteps(selected);
            }
        });
    }

    //Redirect to CookingAssistantViewer activity
    //and pass selected recipe
    public void goToSteps(Recipe selected){
        Intent rbIntent = new Intent(this, CookingAssistantViewer.class);
        rbIntent.putExtra("RECIPE", selected);
        startActivity(rbIntent);
    }


}
