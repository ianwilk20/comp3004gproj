package fooderie.mealPlanner.views;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRoot;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlanner.viewModels.PlanRecipeViewModel;
import fooderie.recipeBrowser.models.Recipe;
import fooderie.recipeBrowser.rbActivity;
import fooderie.recipeBrowser.rbSelected;

import com.example.fooderie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PlanRecipeRecyclerView extends AppCompatActivity {
    private PlanRecipeViewModel m_viewModel;
    private FloatingActionButton m_fab;

    private Toolbar m_toolbar;

    private ItemTouchHelper m_itemOrderTouchHelper;
    private AdapterPlan m_planAdaptor;
    private AdapterRecipe m_planRecipeAdaptor;
    private RecyclerView m_planRecyclerView;
    private RecyclerView m_planRecipeRecyclerView;

    private boolean SELECTING;
    private final Plan ROOT = Plan.getROOT();
    private Plan m_current() {return m_path.peek();}
    private Stack<Plan> m_path = new Stack<>();

    private static final int RECIPE_REQUEST_VIEW = 1;
    private static final String RECIPE_KEY = "RECIPE";
    private static final String REQUEST_RECIPE_KEY = "FROMPLAN";
    private static final String REQUEST_RECIPE_VALUE_YES = "yes";
    private static final String REQUEST_RECIPE_VALUE_NO = "no";

    public static final String LOOKING_FOR_PLANWEEK_KEY = "plan_week";
    public static final String PLANWEEK_KEY = "plan_week";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_recyclerview);

        m_toolbar = findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
        //m_searchbar = new SearchView(this);
        //m_searchbar
        //m_toolbar.addView(m_searchbar);

        m_toolbar.setNavigationOnClickListener( v -> selectParentPlan());
        m_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);

        AppCompatActivity activity = this;
        m_itemOrderTouchHelper = new ItemTouchHelper( new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
                private List<Pair<Integer, Integer>> moves;
                @Override
                @SuppressWarnings("all")
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder from, @NonNull RecyclerView.ViewHolder to) {
                    final RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    final int fromPos = from.getAdapterPosition();
                    final int toPos = to.getAdapterPosition();

                    Pair<Integer, Integer> move = new Pair<>(fromPos, toPos);
                    if (moves == null) {
                        moves = new ArrayList<>();
                    }
                    moves.add(move);

                    adapter.notifyItemMoved(fromPos, toPos);

                    return true;
                }
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // -- NOT REQUIRED -- //
                }
                @Override
                public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                    super.clearView(recyclerView, viewHolder);
                    m_viewModel.updatePlanMealsOrder(activity, m_current().getPlanId(), moves);
                    moves = null;
                }
        });

        Intent intent = getIntent();
        SELECTING = intent.getBooleanExtra(LOOKING_FOR_PLANWEEK_KEY, false);

        m_planAdaptor = new AdapterPlan(this, this::selectPlan, this::deletePlan, (SELECTING) ? this::selectPlanWeek : null, this::updatePlan);
        m_planRecipeAdaptor = new AdapterRecipe(this, getResources(), this::deletePlanRecipe, this::displayRecipe);

        m_planRecipeRecyclerView = findViewById(R.id.PlanRecipeRecyclerView);
        m_planRecipeRecyclerView.setAdapter(m_planRecipeAdaptor);
        m_planRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_planRecyclerView = findViewById(R.id.PlanRecyclerView);
        m_planRecyclerView.setAdapter(m_planAdaptor);
        m_planRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_fab = findViewById(R.id.PlanRecyclerViewFab);
        m_fab.setOnClickListener((View view) -> addPlanDialog());

        m_viewModel = new ViewModelProvider(this).get(PlanRecipeViewModel.class);

        m_path.add(ROOT);
        updateDisplay();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECIPE_REQUEST_VIEW) {
            if (resultCode != RESULT_OK)
                return;

            // -- Get recipe to put into database -- //
            Recipe r = (Recipe) data.getSerializableExtra(RECIPE_KEY);
            m_viewModel.insertRecipeAndPlanRecipe(m_current().getPlanId(), r);
        }
    }

    private Void updatePlan(Plan p) {
        if (p instanceof PlanMeal) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                ((PlanMeal) p).setHour(hourOfDay);
                ((PlanMeal) p).setMinute(minute);
                m_viewModel.updatePlan(p);
            }, ((PlanMeal) p).getHour(), ((PlanMeal) p).getMinute(), false);
            timePickerDialog.show();
        } else {
            m_viewModel.updatePlan(p);
        }
        return null;
    }

    private Void deletePlan(Plan p) {
        m_viewModel.deletePlan(p);
        return null;
    }

    private Void deletePlanRecipe(String r_id) {
        m_viewModel.deletePlanRecipe(m_current().getPlanId(), r_id);
        return null;
    }

    private Void displayRecipe(Recipe r) {
        // -- Connect to recipe browser(rb) selected (rbSelected) -- //
        Intent rbIntent = new Intent(this, rbSelected.class);
        rbIntent.putExtra(RECIPE_KEY, r);
        rbIntent.putExtra(REQUEST_RECIPE_KEY, REQUEST_RECIPE_VALUE_NO);
        startActivity(rbIntent);
        return null;
    }

    private Void selectPlan(Plan p) {
        m_current().removeLiveData(this);
        m_path.add(p);
        updateDisplay();

        return null;
    }

    private Void selectPlanWeek(PlanWeek p) {
        if (p != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(PLANWEEK_KEY, p.getPlanId().longValue());
            setResult(Activity.RESULT_OK, returnIntent);
        }
        finish();
        return null;
    }

    private void selectParentPlan() {
        if (m_path.size() <= 1) {
            finish();
            return;
        }

        m_path.pop().removeLiveData(this);
        updateDisplay();
    }

    @SuppressWarnings("unchecked")
    private void updateDisplay() {
        // -- Update the display in the toolbar -- //
        m_toolbar.setTitle(m_current().childName() + "s");

        // -- Set up the callback function to properly display the current level of plans -- //
        m_viewModel.setupDisplay(m_current(), this, obj -> {
            if (m_current() instanceof PlanMeal) {
                // -- Displaying at the recipe level. Need to display information differently -- //
                m_planRecyclerView.setVisibility(View.INVISIBLE);
                m_planRecipeRecyclerView.setVisibility(View.VISIBLE);

                m_planRecipeAdaptor.setPlanRecipes((List<Recipe>) obj);

            } else {
                m_planRecyclerView.setVisibility(View.VISIBLE);
                m_planRecipeRecyclerView.setVisibility(View.INVISIBLE);

                try {
                    List plans = (List<Plan>) obj;
                    if (plans.size() != 0) {
                        Plan first = (Plan) plans.get(0);
                        if (first instanceof PlanMeal) {
                            Collections.sort((List<PlanMeal>) plans);
                        }
                    }
                    m_planAdaptor.setPlans(plans);
                } catch (Exception e) {
                    Log.d("BANANA", "Observer onCreate: plans not of type List<Plans>");
                }
            }
        });

        // -- Set up additional features based on the level of plan's set attributes -- //
        if (m_current().isChildEditable() && !SELECTING) {
            m_fab.show();
        } else {
            m_fab.hide();
        }

        if (m_current().isChildDraggable()) {
            m_itemOrderTouchHelper.attachToRecyclerView(m_planRecyclerView);
        } else {
            m_itemOrderTouchHelper.attachToRecyclerView(null);
        }
    }

    private void addPlanDialog() {
        if (m_current() instanceof PlanMeal) {
            // -- Addition at the recipe level, make activity to get recipe from recipe browser -- //
            Intent rbIntent = new Intent(this, rbActivity.class);
            rbIntent.putExtra(REQUEST_RECIPE_KEY, REQUEST_RECIPE_VALUE_YES);
            startActivityForResult(rbIntent, RECIPE_REQUEST_VIEW);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New " + m_current().childName() + ":");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
                String planName = input.getText().toString();

                Plan p = null;
                if (m_current() instanceof PlanDay) {
                    // -- Additions at the meal plan level. Need to add order attribute.
                    p = new PlanMeal(m_current().getPlanId(), planName, 0, m_planAdaptor.getItemCount());
                } else if (m_current() instanceof PlanWeek) {
                    p = new PlanDay(m_current().getPlanId(), planName, 0);
                } else if (m_current() instanceof PlanRoot) {
                    p = new PlanWeek(planName, 0);
                }

                if (p != null)
                    m_viewModel.insertPlan(p);

            }));
            builder.setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> dialog.dismiss()));

            builder.show();
        }
    }
}