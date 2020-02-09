package fooderie.CookingAssistant.views;

import com.example.fooderie.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CookingAssistantViewer extends AppCompatActivity
{
    private static final String TAG = CookingAssistantViewer.class.getSimpleName();
    TextView tstTestView;
    ListView insList;
    String instructionList[] = {"1. Boil Water", "2. Open Raman Pack", "3. Pour half of water into a bowl", "4. Put Raman into boiling water to cook", "5. Put flavour packets into bowl water", "6. Wait until Raman cooked", "7. Strain raman and put into bowl"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_viewer);
        tstTestView = findViewById(R.id.tempList);

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
        String listText = "";

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                Document doc = Jsoup.connect("https://food52.com/recipes/22633-strawberry-basil-lemonade").get();
                //Elements recipe = doc.select("#recipeDirectionsRoot > div:eq(1) > ol");
                Elements recipeList = doc.select("ol");

                int count = 0;
                for (Element recipeL : recipeList)
                {
                    for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                    {
                        count += 1;

                        Node step1 = recipeL.childNode(i);
                        Node step2 = step1.childNode(1);
                        Node step3 = step2.childNode(0);
                        String val = step3.toString();
                        listText += count + ". " + val + "\n\n";

                    }
                }

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
            tstTestView.setText(listText);

        }
    }
}