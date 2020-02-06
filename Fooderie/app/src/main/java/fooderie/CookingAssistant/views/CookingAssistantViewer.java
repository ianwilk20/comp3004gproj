package fooderie.CookingAssistant.views;

import com.example.fooderie.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CookingAssistantViewer extends AppCompatActivity
{
    private static final String TAG = CookingAssistantViewer.class.getSimpleName();

    ListView insList;
    String instructionList[] = {"1. Boil Water", "2. Open Raman Pack", "3. Pour half of water into a bowl", "4. Put Raman into boiling water to cook", "5. Put flavour packets into bowl water", "6. Wait until Raman cooked", "7. Strain raman and put into bowl"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_viewer);

        Log.d(TAG,"\n\n\n ----------------");
        new jSoupParse().execute();


        //insList = findViewById(R.id.lstInstructions);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_cooking_assistant_viewer, R.id.txtInsOverview, instructionList);
        //insList.setAdapter(arrayAdapter);
    }

    public class jSoupParse extends AsyncTask<Void, Void, Void>
    {
        String words;
        String title;

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                Document doc = Jsoup.connect("https://food52.com/recipes/22633-strawberry-basil-lemonade").get();
                //Elements recipe = doc.select("#recipeDirectionsRoot > div:eq(1) > ol");
                Elements recipe = doc.select("ol");

                for (Element element : recipe)
                {
                    Log.d(TAG, element.html());
                }

                title = doc.title();
                words = doc.text();

            }
            catch(Exception e)
            {
                //e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "done");
            //Log.d(TAG, title);

        }
    }
}