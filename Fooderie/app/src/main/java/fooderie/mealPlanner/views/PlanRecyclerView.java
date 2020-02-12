package fooderie.mealPlanner.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRoot;
import fooderie.mealPlanner.viewModels.PlanViewModel;


import com.example.fooderie.R;

import java.util.List;
import java.util.Stack;

public class PlanRecyclerView extends AppCompatActivity {
    private final String m_TAG = PlanRecyclerView.class.getSimpleName();
    private final Plan ROOT = new PlanRoot();

    private PlanViewModel m_viewModel;
    private FloatingActionButton m_fab;
    private PlanAdapter m_adaptor;

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


        m_adaptor = new PlanAdapter(this, this::selectPlan, this::deletePlan);
        RecyclerView recyclerView = findViewById(R.id.PlanRecyclerView);
        recyclerView.setAdapter(m_adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_fab = findViewById(R.id.fab);
        m_fab.setOnClickListener((View view) -> addPlanDialog());

        m_viewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        m_path.add(ROOT);
        selectPlan(m_current());
    }

    private Void deletePlan(Plan p) {
        m_viewModel.deletePlan(p);
        return null;
    }

    private void selectParentPlan() {
        if (m_path.size() <= 1)
            return;

        m_path.pop().removeLiveData(this);
        updateDisplay();
    }

    private Void selectPlan(Plan p) {
        m_path.add(p);
        updateDisplay();

        return null;
    }

    @SuppressWarnings("unchecked")
    private void updateDisplay() {
        m_viewModel.setupDisplay(m_current(), this, plans -> {
            try {
                m_adaptor.setPlans((List<Plan>) plans);
            } catch (Exception e) {
                Log.d("BANANA", "Observer onCreate: plans not of type List<Plans>");
            }
        });

        if (m_current().isChildEditable()) {
            m_fab.show();
        } else {
            m_fab.hide();
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

            if (!(m_current() instanceof PlanMeal)) {
                Plan p = m_current().makeChild(m_current().getPlanId(), planName, 0);
                m_viewModel.insertPlan(p);
            } else {
                // -- At the recipe level, need to acquire information differently and store differently -- //
            }

        }));
        builder.setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> dialog.dismiss()));

        builder.show();
    }
    /*
    private Void deletePlan(Plan p) {
        m_viewModel.deletePlan(p.getId());
        return null;
    }

    private Void displayChildrenPlansOfID(Plan p) {
        // -- REMOVE OLD LIVEDATA OBSERVERS -- //
        Log.d("BANANA", "displayChildrenPlansOfID: BEFORE " + m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).hasActiveObservers());
        m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).removeObservers(this);
        Log.d("BANANA", "displayChildrenPlansOfID: AFTER " + m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).hasActiveObservers());
        // -- MAKE NEW OBSERVER FOR THE CHILDREN OF THE CURRENT PARENT PLAN -- //
        m_parentPlan = (p == null) ? ROOT : p;
        m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).observe(this, this::displayPlans);
        Log.d("BANANA", "displayChildrenPlansOfID: AFTER AFTER " + m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).hasActiveObservers());

        // -- DISPLAY FAB IF REQUIRED -- //
        if (Depth.isEditableDepth(m_parentPlan)) {
            m_fab.show();
        } else {
            m_fab.hide();
        }

        return null;
    }

    private void displayPlans(List<Plan> plans) {
        m_adaptor.setPlans(plans);
    }*/
}