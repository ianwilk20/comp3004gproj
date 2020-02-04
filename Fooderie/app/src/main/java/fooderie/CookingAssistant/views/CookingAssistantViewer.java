package fooderie.CookingAssistant.views;

import com.example.fooderie.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class CookingAssistantViewer extends AppCompatActivity
{
    //Get recipe to use

    //Parse instructions
    ListView insList;
    String instructionList[] = {"1. Boil Water", "2. Open Raman Pack", "3. Pour half of water into a bowl", "4. Put Raman into boiling water to cook", "5. Put flavour packets into bowl water", "6. Wait until Raman cooked", "7. Strain raman and put into bowl"};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_viewer);

        insList = findViewById(R.id.lstInstructions);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_cooking_assistant_viewer, R.id.txtInsOverview, instructionList);
        insList.setAdapter(arrayAdapter);
    }
}