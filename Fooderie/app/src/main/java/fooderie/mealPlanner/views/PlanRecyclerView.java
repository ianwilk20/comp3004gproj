package fooderie.mealPlanner.views;

import android.content.DialogInterface;
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
import fooderie.mealPlanner.viewModels.PlanViewModel;
import kotlin.TuplesKt;


import com.example.fooderie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PlanRecyclerView extends AppCompatActivity {
    private PlanViewModel m_viewModel;
    private FloatingActionButton m_fab;

    private ItemTouchHelper m_itemOrderTouchHelper;
    private PlanAdapter m_planAdaptor;
    private RecyclerView m_recyclerView;

    private final Plan ROOT = new PlanRoot();
    private Plan m_current() {return m_path.peek();}
    private Stack<Plan> m_path = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_right_black_24dp);
        toolbar.setNavigationOnClickListener( v -> selectParentPlan());

        m_itemOrderTouchHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
                private List<Pair<Integer, Integer>> moves;
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder from, @NonNull RecyclerView.ViewHolder to) {
                    final RecyclerView.Adapter adapter = (PlanAdapter) recyclerView.getAdapter();
                    final int fromPos = from.getAdapterPosition();
                    final int toPos = to.getAdapterPosition();

                    Pair<Integer, Integer> move = new Pair<Integer, Integer>(fromPos, toPos);
                    if (moves == null) {
                        moves = new ArrayList<Pair<Integer, Integer>>();
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
                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    super.clearView(recyclerView, viewHolder);
                    m_viewModel.updatePlanMealsOrder(m_current().getPlanId(), moves);
                    moves = null;
                }
        });
        m_planAdaptor = new PlanAdapter(this, this::selectPlan, this::deletePlan, this::updatePlan);
        m_recyclerView = findViewById(R.id.PlanRecyclerView);
        m_recyclerView.setAdapter(m_planAdaptor);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_fab = findViewById(R.id.fab);
        m_fab.setOnClickListener((View view) -> addPlanDialog());

        m_viewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        m_path.add(ROOT);
        selectPlan(m_current());
    }

    private Void selectPlan(Plan p) {
        m_path.add(p);
        updateDisplay();

        return null;
    }

    private Void deletePlan(Plan p) {
        m_viewModel.deletePlan(p);
        return null;
    }

    private Void updatePlan(Plan p) {
        m_viewModel.updatePlan(p);
        return null;
    }

    private void selectParentPlan() {
        if (m_path.size() <= 1)
            return;

        m_path.pop().removeLiveData(this);
        updateDisplay();
    }

    @SuppressWarnings("unchecked")
    private void updateDisplay() {
        m_viewModel.setupDisplay(m_current(), this, obj -> {
            try {
                List plans = (List<Plan>) obj;
                if (plans.size() != 0) {
                    Plan first = (Plan) plans.get(0);
                    if (first instanceof PlanMeal) {
                        Collections.sort((List<PlanMeal>)plans);
                    }
                }
                m_planAdaptor.setPlans(plans);
            } catch (Exception e) {
                Log.d("BANANA", "Observer onCreate: plans not of type List<Plans>");
            }
        });

        if (m_current().isChildEditable()) {
            m_fab.show();
        } else {
            m_fab.hide();
        }

        if (m_current().isChildDraggable()) {
            m_itemOrderTouchHelper.attachToRecyclerView(m_recyclerView);
        } else {
            m_itemOrderTouchHelper.attachToRecyclerView(null);
        }
    }

    private void addPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New "+ m_current().childName()+":");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
            String planName = input.getText().toString();

            Plan p = null;
            if (m_current() instanceof PlanMeal) {
                // -- Additions at the recipe level. Need to acquire information differently and store differently -- //

            } else if (m_current() instanceof PlanDay) {
                // -- Additions at the meal plan level. Need to add order attribute.
                p = new PlanMeal(m_current().getPlanId(), planName, 0, m_planAdaptor.getItemCount());

            } else if (m_current() instanceof PlanWeek) {
                p = new PlanDay(m_current().getPlanId(), planName, 0);
            } else if (m_current() instanceof PlanRoot) {
                p = new PlanWeek(m_current().getPlanId(), planName, 0);
            }

            if (p != null)
                m_viewModel.insertPlan(p);

        }));
        builder.setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> dialog.dismiss()));

        builder.show();
    }
}