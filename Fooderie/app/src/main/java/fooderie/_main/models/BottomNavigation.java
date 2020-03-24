package fooderie._main.models;

import android.content.Intent;
import android.view.MenuItem;

import com.example.fooderie.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import fooderie._main.MainActivity;
import fooderie.groceryList.views.GroceryListView;
import fooderie.mealPlanner.views.mpPlanRecipeDisplayView;
import fooderie.options.OptionsActivity;
import fooderie.recipeBrowser.views.rbSearch;

public class BottomNavigation {
    private final AppCompatActivity activity;

    public BottomNavigation(AppCompatActivity activity, int index) {
        this.activity = activity;

        BottomNavigationView navigation = (BottomNavigationView) activity.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new listener());
        navigation.getMenu().getItem(index).setChecked(true);
    }

    private class listener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_options:
                    intent = new Intent(activity, OptionsActivity.class);
                    break;
                case R.id.action_search:
                    intent = new Intent(activity, rbSearch.class);
                    intent.putExtra("FROMPLAN", "no");
                    break;
                case R.id.action_mealPlanner:
                    intent = new Intent(activity, mpPlanRecipeDisplayView.class);
                    break;
                case R.id.action_grocery:
                    intent = new Intent(activity, GroceryListView.class);
                    break;
                default:
                    intent = new Intent(activity, MainActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            return false;
        }
    }
}
