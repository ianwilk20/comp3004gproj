package fooderie.cookingAssistant.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooderie.R;
import com.squareup.picasso.Picasso;

import fooderie.recipeBrowser.models.Recipe;


public class CookingAssistantPreview extends AppCompatActivity
{
    private Button startButton;
    //private TextView ingridentText;
    private ListView ingridentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Get selected recipe from rbSearch
        Intent intent = getIntent();
        Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_pre);
        startButton = findViewById(R.id.btnStart);

        //Image
        ImageView recipeImage = findViewById(R.id.imgPreview);
        Picasso.get().load(selected.image).into(recipeImage);

        //Ingredients list
        ingridentList = findViewById(R.id.ingridentView);
        ArrayAdapter<String> rbArrAdapt = new ArrayAdapter(CookingAssistantPreview.this, android.R.layout.simple_list_item_1, selected.theIngredients);
        ingridentList.setAdapter(rbArrAdapt);

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
