package fooderie.CookingAssistant.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooderie.R;

import fooderie.recipeBrowser.models.Recipe;


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
        TODO: FILL IN IMAGE PREVIEW HERE
        ImageView recipeImage = findViewById(R.id._____);
        Picasso.get().load(selected.image).into(recipeImage);
         */

        String steps = "Ingredients: \n";
        for(String ingredient : selected.theIngredients)
        {
            steps += "- " + ingredient + "\n";
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
