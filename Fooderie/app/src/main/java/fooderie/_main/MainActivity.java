package fooderie._main;

import android.os.Bundle;

import com.example.fooderie.R;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import fooderie.mealPlanner.models.PlanMeal;
import fooderie.scheduler.views.sTodayMealFragment;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.views.sWeeklyScheduleFragment;

public class MainActivity extends AppCompatActivity implements
    sTodayMealFragment.OnListFragmentInteractionListener,
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
