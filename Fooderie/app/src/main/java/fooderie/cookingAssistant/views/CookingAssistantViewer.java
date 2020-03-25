package fooderie.CookingAssistant.views;

import fooderie._main.MainActivity;
import com.example.fooderie.R;

import fooderie.CookingAssistant.models.FavouriteClick;
import fooderie.CookingAssistant.viewModels.CookingAssistantViewerViewModel;
import fooderie.recipeBrowser.models.Recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import fooderie.recipeBrowser.viewModels.RBViewModel;

import android.widget.Toast;

/* TODO: fix for this 'https://www.seriouseats.com/recipes/2013/04/eggs-in-a-bacon-basket-recipe.html' */
public class CookingAssistantViewer extends AppCompatActivity
{
    private static final String TAG = CookingAssistantViewer.class.getSimpleName();
    ArrayList<String> instructionList = new ArrayList<>();
    int numSteps = 0;
    String selectedUrl = "";

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private fooderie.CookingAssistant.views.SliderAdapter sliderAdapter;
    private TextView[] mDots;

    Context context;
    Button btnMenu;
    Button btnFavourite;
    Button btnTimer;
    Recipe selected;
    FavouriteClick favClick;
    ProgressBar progressBar;

    private RBViewModel rbViewModel;
    private CookingAssistantViewerViewModel caViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "\n\n\n --------");  //Log to indicate we have started the viewer

        //Get view models
        rbViewModel = ViewModelProviders.of(this).get(RBViewModel.class);
        caViewModel = ViewModelProviders.of(this).get(CookingAssistantViewerViewModel.class);

        //Get selected recipe from rbSearch
        Intent intent = getIntent();
        selected = (Recipe)intent.getSerializableExtra("RECIPE");
        selectedUrl = selected.url;
        new jSoupParse().execute();
        context = this;

        //Set our context
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant);

        //Crete our favClicker
        favClick = new FavouriteClick(selected, rbViewModel);

        //Have to reset invisibility multiple times as the async return view breaks it
        btnMenu = findViewById(R.id.btnMainMenu);
        View v = findViewById(android.R.id.content);
        btnMenu.setVisibility(v.GONE);

        btnFavourite = findViewById(R.id.btnFavourite);
        btnFavourite.setVisibility(v.GONE);

        progressBar = findViewById(R.id.loadIndicator);
        progressBar.setVisibility(View.VISIBLE);

        btnTimer = findViewById(R.id.btnOpenTimer);
        btnTimer.setVisibility(v.GONE);
    }

    //Open timer clicked
    public void btnOpenStartTimer(View v)
    {
        goToTimer();
    }

    public void goToTimer()
    {
        Intent rbIntent = new Intent(this, fooderie.CookingAssistant.views.CookingAssistantTimerView.class);
        startActivity(rbIntent);
    }

    //Favourite button clicked
    public void btnFavClick(View v)
    {
        Log.d(TAG, "Add/Delete from favourites");
        String toastDisplay = favClick.favouriteClicked();
        Toast.makeText(CookingAssistantViewer.this, toastDisplay, Toast.LENGTH_SHORT).show();
    }

    //Menu button on click
    public void onClick(View v)
    {
        Log.d(TAG, "\n\n\n RETURN TO MAIN MENU");
        goToSteps(selected);

    }

    //Go to the main menu
    public void goToSteps(Recipe selected)
    {
        Intent intent = new Intent();
        intent.putExtra("RECIPE", selected);
        setResult(RESULT_OK, intent);
        finish();
    }

    //JSoupParsing the website to get the instructions
    public class jSoupParse extends AsyncTask<Void, Void, Void>
    {
        //String url = getUrl();
        String url = selectedUrl;
        String val = "";
        ArrayList<String> returnIns = new ArrayList<String>();

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                Document doc = Jsoup.connect(url).get();

                if (url.contains("food52"))     //Parse food52 websites
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
                            returnIns.add(val);
                        }
                    }
                }
                else if (url.contains("foodnetwork"))   //Parse foodnetwork websites
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
                            returnIns.add(val);
                        }
                    }
                }

                else if (url.contains("seriouseats"))   //Parse seriouseats websites
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

                            if (step2.toString().contains("<"))
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

                            returnIns.add(val);
                        }
                    }
                }
                else
                    Log.d(TAG, "Non parsable website, do something here...");

            }
            catch(Exception e)
            {
                //e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)    //When our parsing finishes
        {
            //Log and set new view
            super.onPostExecute(aVoid);
            instructionList = returnIns;
            instructionList.add("Finished! \n");
            setContentView(R.layout.activity_cooking_assistant);

            //Find our elements we are modifiying
            mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
            mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

            //Set our element values
            sliderAdapter = new fooderie.CookingAssistant.views.SliderAdapter(context, instructionList);
            mSlideViewPager.setAdapter((sliderAdapter));
            numSteps = instructionList.size();
            addStepStatus(numSteps, 0);

            mSlideViewPager.addOnPageChangeListener(viewListener);

            View v = findViewById(android.R.id.content);

            //Show timer and hide progress bar when everything is loaded
            btnTimer = findViewById(R.id.btnOpenTimer);
            btnTimer.setVisibility(v.VISIBLE);

            progressBar = findViewById(R.id.loadIndicator);
            progressBar.setVisibility(View.GONE);

            //Hide the menu button (only shown on last page)
            btnMenu = findViewById(R.id.btnMainMenu);
            btnMenu.setVisibility(v.GONE);

            //Hide the favourite button (only shown on last page)
            btnFavourite = findViewById(R.id.btnFavourite);
            btnFavourite.setVisibility(v.GONE);
        }
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

        if (btnMenu != null)    //If we have our btnMenu created
        {
            if (position == mDots.length - 1)   //If we are on the last page, show the done button, if not, don't
                btnMenu.setVisibility(View.VISIBLE);
            else
                btnMenu.setVisibility(View.GONE);
        }
        if (btnMenu != null)    //If we have our btnFavourite created
        {
            if (position == mDots.length - 1)   //If we are on the last page, show the done button, if not, don't
                btnFavourite.setVisibility(View.VISIBLE);
            else
                btnFavourite.setVisibility(View.GONE);
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