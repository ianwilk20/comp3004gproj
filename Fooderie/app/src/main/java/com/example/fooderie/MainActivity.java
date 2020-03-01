package com.example.fooderie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import fooderie.CookingAssistant.views.CookingAssistantPreview;
import fooderie.groceryList.views.GroceryListView;
import fooderie.recipeBrowser.rbActivity;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.views.PlanRecipeRecyclerView;
import fooderie.mealPlanner.views.TodayMealFragment;
import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.mealPlannerScheduler.views.WeeklyScheduleFragment;

public class MainActivity extends AppCompatActivity implements
    TodayMealFragment.OnListFragmentInteractionListener,
    WeeklyScheduleFragment.OnListFragmentInteractionListener {

    private Button groceryListButton;
    private Button mealPlannerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Click Listener for options button
        Button optionsButton = findViewById(R.id.options);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOptions();
            }
        });

        final SearchView search = findViewById(R.id.search);
        search.setFocusableInTouchMode(false);
        search.clearFocus();
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecipeBrowser();
                search.clearFocus();
            }
        });

        groceryListButton = findViewById(R.id.groceryButton);
        groceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGroceryList();
            }
        });

        final AppCompatActivity tmp = this;
        mealPlannerButton = findViewById(R.id.mealPlannerButton);
        mealPlannerButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(tmp, PlanRecipeRecyclerView.class);
            startActivity(intent);
        });
    }

    public void openRecipeBrowser(){
        Intent rbIntent = new Intent(this, rbActivity.class);
        rbIntent.putExtra("FROMPLAN", "no");
        startActivity(rbIntent);
    }

    public void openGroceryList(){
        Intent intent = new Intent(this, GroceryListView.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(PlanMeal meal) {
    }

    @Override
    public void onListFragmentInteraction(Schedule s) {
    }

    //Show set_preferences xml in an Alert Dialog
    private void setOptions() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
