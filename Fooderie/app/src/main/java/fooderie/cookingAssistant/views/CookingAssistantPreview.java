package fooderie.CookingAssistant.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooderie.R;
import com.squareup.picasso.Picasso;

import fooderie._main.BottomNavigation;
import fooderie.recipeBrowser.models.Recipe;


public class CookingAssistantPreview extends AppCompatActivity
{
    private Button startButton;
    private TextView title;
    private ListView ingridentList;
    public static final int REQUEST_CODE = 1;

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

        //Get recipe name
        title = findViewById(R.id.recipeTitle);
        title.setText(selected.getLabel());


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

        BottomNavigation navigation = new BottomNavigation(this, 1);

    }

    //Redirect to CookingAssistantViewer activity
    //and pass selected recipe
    public void goToSteps(Recipe selected)
    {
        Intent intent = new Intent(this, CookingAssistantViewer.class);
        intent.putExtra("RECIPE", selected);
        startActivityForResult(intent , REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        try
        {
            super.onActivityResult(requestCode, resultCode, intent);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) //On return, return to whatever called preview
            {
                Recipe selected = (Recipe)intent.getSerializableExtra("RECIPE");
                finish();
            }
        }
        catch (Exception ex)
        {
        }

    }
}
