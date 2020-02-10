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
        String url2 = "https://food52.com/recipes/22633-strawberry-basil-lemonade";
        String url3 = "https://www.foodnetwork.com/recipes/food-network-kitchen/strawberries-with-basil-granita-recipe-1928457";
        String url4 = "https://www.seriouseats.com/recipes/2013/06/whole-wheat-oatmeal-pancakes-maple-roast-rhubarb-recipe.html";
        String url = "https://www.seriouseats.com/recipes/2015/06/grilled-scallion-pancake-recipe.html";
        String val = "";

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                Document doc = Jsoup.connect(url).get();
                //Elements recipe = doc.select("#recipeDirectionsRoot > div:eq(1) > ol");

                if (url.contains("food52"))
                {
                    Elements recipeList = doc.select("ol");
                    int count = 0;
                    for (Element recipeL : recipeList)
                    {
                        for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                        {
                            count++;

                            Node step1 = recipeL.childNode(i);
                            Node step2 = step1.childNode(1);
                            Node step3 = step2.childNode(0);
                            String val = step3.toString();
                            listText += count + ". " + val + "\n\n";
                        }
                    }
                }
                else if (url.contains("foodnetwork"))
                {
                    Elements recipeList = doc.select("ol");
                    int count = 0;
                    for (Element recipeL : recipeList)
                    {
                        for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                        {
                            count++;

                            Node step1 = recipeL.childNode(i);
                            Node step3 = step1.childNode(0);
                            String val = step3.toString();
                            listText += count + ". " + val + "\n\n";
                        }
                    }
                }

                else if (url.contains("seriouseats"))
                {
                    Elements recipeList = doc.select("ol");
                    int count = 0;
                    for (Element recipeL : recipeList)
                    {
                        for(int i = 1; i < recipeL.childNodeSize(); i += 2) //Skip over the empty child nodes
                        {
                            count++;

                            Node step1 = recipeL.childNode(i);
                            Node step2 = step1.childNode(3).childNode(2).childNode(0);
                            if (step2.toString().contains("<strong>"))
                            {
                                Node step3 = step2.childNode(0); //Get actual step info
                                Node step4 = step1.childNode(3).childNode(2).childNode(1); //Get actual step info

                                val = step3.toString() + ":" + step4.toString();

                            }
                            else
                            {
                                Node step4 = step1.childNode(3).childNode(2).childNode(0); //Get actual step info


                                val = step4.toString();

                            }

                            listText += count + " - " + val + "\n\n";
                        }
                    }
                }
                else
                {
                    Log.d(TAG, "Non parsable website, do something here...");

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
            Log.d(TAG, listText);
        }
    }
}