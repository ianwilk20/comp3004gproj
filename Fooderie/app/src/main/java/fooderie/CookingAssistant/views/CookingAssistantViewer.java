package fooderie.CookingAssistant.views;

import com.example.fooderie.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CookingAssistantViewer extends AppCompatActivity
{
    private static final String TAG = CookingAssistantViewer.class.getSimpleName();
    //TextView tstTestView;
    //ListView insList;
    //String instructionList[] = {"1. Boil Water", "2. Open Raman Pack", "3. Pour half of water into a bowl", "4. Put Raman into boiling water to cook", "5. Put flavour packets into bowl water", "6. Wait until Raman cooked", "7. Strain raman and put into bowl"};
    ArrayList<String> instructionList = null;
    //JSoupParse jSoupParse;
    int numSteps = 3;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "\n\n\n --------");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant);

        boolean running = false;
        try
        {
            //Parse with JSoup
            new jSoupParse().execute().get();
        }
        catch(Exception e)
        {

        }



        Log.d(TAG, instructionList.toString());

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new SliderAdapter(this, instructionList);
        mSlideViewPager.setAdapter((sliderAdapter));
        numSteps = instructionList.size();
        addStepStatus(numSteps, 0);

        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    private void getParse()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {

                }
                catch (Exception e)
                {

                }
            }
        }).start();
    }

    public class jSoupParse extends AsyncTask<Void, Void, Void>
    {
        String listText = "";
        String url = "https://www.seriouseats.com/recipes/2015/06/grilled-scallion-pancake-recipe.html";
        String val = "";
        ArrayList<String> returnIns = new ArrayList<String>();

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                Document doc = Jsoup.connect(url).get();

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
                            //listText += count + ". " + val + "\n\n";
                            returnIns.add(val);
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
                            //listText += count + ". " + val + "\n\n";
                            returnIns.add(val);
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
                            //listText += count + " - " + val + "\n\n";
                            returnIns.add(val);
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
            instructionList = returnIns;
            Log.d(TAG, listText);
        }
    }

    public String getUrl()
    {
        Random rng = new Random();

        String[] urls = {
                "https://food52.com/recipes/22633-strawberry-basil-lemonade",
                "https://www.foodnetwork.com/recipes/food-network-kitchen/strawberries-with-basil-granita-recipe-1928457",
                "https://www.seriouseats.com/recipes/2013/06/whole-wheat-oatmeal-pancakes-maple-roast-rhubarb-recipe.html",
                "https://www.seriouseats.com/recipes/2015/06/grilled-scallion-pancake-recipe.html"
        };

        return "https://food52.com/recipes/22633-strawberry-basil-lemonade";
    }

    public void addStepStatus(int stepLength, int position) // From https://www.youtube.com/watch?v=byLKoPgB7yA
    {
        mDots = new TextView[stepLength];   //Get our dot view
        mDotLayout.removeAllViews();    //Removes old dots and colours

        for(int i = 0; i < mDots.length; i++)   //For all our dots, draw them and set colour if we are on that page
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;")); //Set shape
            mDots[i].setTextSize(40);   //Set size
            if (i == position)
                mDots[i].setTextColor(getResources().getColor(R.color.colorSelected_Dark));
            else
                mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary_Dark));

            mDotLayout.addView(mDots[i]);   //Add to our view
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            addStepStatus(numSteps, position);
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    };
}