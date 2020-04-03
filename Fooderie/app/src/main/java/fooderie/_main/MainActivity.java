package fooderie._main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.fooderie.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import java.util.List;

import fooderie._main.models.BottomNavigation;
import fooderie._main.models.FooderieDao;
import fooderie._main.models.FooderieRepository;
import fooderie.groceryList.views.GroceryListView;
import fooderie._main.models.NotificationHelper;
import fooderie.options.OptionsActivity;
import fooderie.recipeBrowser.views.rbSearch;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.views.mpPlanRecipeDisplayView;
import fooderie.mealPlanner.views.mpTodayMealFragment;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.views.sWeeklyScheduleFragment;

public class MainActivity extends AppCompatActivity implements
    mpTodayMealFragment.OnListFragmentInteractionListener,
    sWeeklyScheduleFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigation navigation = new BottomNavigation(this, 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationHelper nh = new NotificationHelper(this);
        nh.InitMealPlannerNotification();
    }

    @Override
    public void onListFragmentInteraction(PlanMeal meal) {
    }

    @Override
    public void onListFragmentInteraction(Schedule s) {
    }
}
