package com.example.fooderie;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import fooderie.CookingAssistant.views.CookingAssistantViewer;
import fooderie.groceryList.GroceryList;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.views.PlanRecipeRecyclerView;
import fooderie.mealPlanner.views.TodayMealRecyclerViewFragment.OnListFragmentInteractionListener;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private Button groceryListButton;
    private Button mealPlannerButton;
    private Button cookingAssistantButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        groceryListButton = findViewById(R.id.groceryButton);
        groceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGroceryList();
            }
        });

        final AppCompatActivity cAssistThis = this;
        cookingAssistantButton = findViewById(R.id.cookingAssistantButton);
        cookingAssistantButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(cAssistThis, CookingAssistantViewer.class);
                startActivity(intent);
            }
        });


        final AppCompatActivity tmp = this;
        mealPlannerButton = findViewById(R.id.mealPlannerButton);
        mealPlannerButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(tmp, PlanRecipeRecyclerView.class);
            startActivity(intent);
        });
    }

    public void openRecipeBrowser(View rbView){
        Intent rbIntent = new Intent(this, rbActivity.class);
        startActivity(rbIntent);
    }

    public void openGroceryList(){
        Intent intent = new Intent(this, GroceryList.class);
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
}
