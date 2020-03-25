package fooderie._main.models;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
    private final BottomNavigationView navigation;
    private final int index;

    public BottomNavigation(AppCompatActivity a, int i) {
        activity = a;
        index = i;

        navigation = activity.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new listener());
        navigation.getMenu().getItem(index).setChecked(true);
    }

    public void hideNavigation() {
        navigation.setVisibility(View.INVISIBLE);
    }

    private int getMenuItemIndex(final @NonNull MenuItem item) {
        Menu m = navigation.getMenu();
        for (int i = 0; i < m.size(); i++) {
            if (m.getItem(i) == item) {
                return i;
            }
        }
        return -1;
    }

    private class listener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(final @NonNull MenuItem item) {
            int selectedIndex = getMenuItemIndex(item);
            if (selectedIndex < 0 || selectedIndex == index)
                return false;

            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_options:
                    intent = new Intent(activity, OptionsActivity.class);
                    break;
                case R.id.action_search:
                    intent = new Intent(activity, rbSearch.class);
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

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);

            int anim = (index - selectedIndex > 0) ? R.anim.slide_in_left : R.anim.slide_in_right;
            activity.overridePendingTransition(anim, anim);

            return false;
        }
    }
}
