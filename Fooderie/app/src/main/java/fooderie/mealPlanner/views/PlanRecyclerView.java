package fooderie.mealPlanner.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.viewModels.PlanViewModel;


import com.example.fooderie.R;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public class PlanRecyclerView extends AppCompatActivity {
    //private final Long PLAN_WEEK_LEVEL = null;
    private final Long PLAN_MEAL_LEVEL = 2L;
    private final String m_TAG = PlanRecyclerView.class.getSimpleName();

    private PlanViewModel m_viewModel;
    private FloatingActionButton m_fab;
    private Long m_parentId;
    private PlanAdapter m_adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_adaptor = new PlanAdapter(this, this::navigateToID);
        RecyclerView recyclerView = findViewById(R.id.PlanRecyclerView);
        recyclerView.setAdapter(m_adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_parentId = null;

        m_viewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        m_viewModel.getAllPlans(this).observe(this, this::setPlans);

        m_fab = findViewById(R.id.fab);
        m_fab.setOnClickListener((View view) -> addPlanDialog());

        // -- Pre-populate Database -- //
        //m_viewModel.deleteAllPlans();

        //m_viewModel.insert(new Plan(m_parentId, "THIS IS A TEST"));
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS NOT A TEST"));
        m_viewModel.insert(new Plan(m_parentId, "THIS IS MAYBE A TEST"));
    }

    private void addPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (m_parentId == null)
            builder.setTitle("Weekly Meal Plan Name:");
        else if (m_parentId.equals(PLAN_MEAL_LEVEL))
            builder.setTitle("Meal Name:");
        else
            builder.setTitle("UNKNOWN");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
            String planName = input.getText().toString();
            m_viewModel.insert(new Plan(m_parentId,planName));
        }));
        builder.setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> dialog.dismiss()));

        builder.show();
    }

    private boolean setPlans(@Nullable List<Plan> plans) {
        if (plans == null)
            return false;

        // -- FILTER PLANS TO THOSE WITH THE SAME PARENT ID AND LEVEL -- //
        List<Plan> display_plans = plans.stream().filter(p -> {
            if (m_parentId == null) {
                return p.getParentId() == null;
            } else {
                return m_parentId.equals(p.getParentId());
            }
        }).collect(Collectors.toList());

        // -- DISPLAY ONLY THE PLANS FOR THE GIVEN LEVEL -- //
        m_adaptor.setPlans(display_plans);
        //Log.d(m_TAG, "setPlans: "+display_plans.toString());
        return true;
    }

    private Void navigateToID(Long p_id) {
        m_parentId = p_id;
        if (m_parentId == null || PLAN_MEAL_LEVEL.equals(getCurrentLevel(p_id))) {
            m_fab.show();
        } else {
            m_fab.hide();
        }

        // -- UPDATE DISPLAYED PLANS -- //
        if (setPlans(m_viewModel.cachedPlans)) {
            Log.d(m_TAG, "navigateToID: THERE WAS A LIST OF VALUES IN PLANS");
        } else {
            Log.d(m_TAG, "navigateToID: NO PLANS");
        }

        return null;
    }

    private Long getCurrentLevel(Long p_id) {
        if (p_id == null)
            return 0L;

        for (Plan p : m_viewModel.cachedPlans) {
            if (p_id.equals(p.getParentId())) {
                Long i = getCurrentLevel(p.getParentId());
                if (i == null)
                    return null;
                else
                    return 1L + i;
            }
        }

        return null;
    }
}