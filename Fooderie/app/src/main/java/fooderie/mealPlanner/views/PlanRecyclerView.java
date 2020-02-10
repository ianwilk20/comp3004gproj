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
import fooderie.mealPlanner.models.Level;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.viewModels.PlanViewModel;


import com.example.fooderie.R;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlanRecyclerView extends AppCompatActivity {
    private final String m_TAG = PlanRecyclerView.class.getSimpleName();

    private PlanViewModel m_viewModel;
    private FloatingActionButton m_fab;
    private Long m_parentId;
    private PlanAdapter m_adaptor;
    private List<Level> m_levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_adaptor = new PlanAdapter(this, this::displayChildrenPlansOfID);
        RecyclerView recyclerView = findViewById(R.id.PlanRecyclerView);
        recyclerView.setAdapter(m_adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_parentId = null;

        m_viewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        m_viewModel.plansObserver(this, this::updateChildrenPlansOfID);

        m_fab = findViewById(R.id.fab);
        m_fab.setOnClickListener((View view) -> addPlanDialog());

        m_levels = new ArrayList<>();
        m_levels.add(new Level(0L, "Week Plans", true));
        m_levels.add(new Level(1L, "Day Plans", false));
        m_levels.add(new Level(2L, "Meal Plans", true));

        // -- Pre-populate Database -- //
        //m_viewModel.deleteAllPlans();
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS A TEST"));
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS NOT A TEST"));
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS MAYBE A TEST"));
    }

    private void addPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Level l = getLevelInfo(m_parentId);
        if (l != null) {
            builder.setTitle("New " + l.getName().substring(0, l.getName().length()-1) + " Name");
        } else {
            builder.setTitle("UNKNOWN");
        }

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
            String planName = input.getText().toString();
            m_viewModel.insert(new Plan(m_parentId, planName));
        }));
        builder.setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> dialog.dismiss()));

        builder.show();
    }

    private Void updateChildrenPlansOfID(Void v) {
        displayChildrenPlansOfID(m_parentId);
        return v;
    }

    private Void displayChildrenPlansOfID(Long p_id) {
        List<Plan> plans = m_viewModel.getAllPlans();
        m_parentId = p_id;

        // -- DISPLAY FAB IF REQUIRED -- //
        Level l = getLevelInfo(p_id);
        if (l != null) {
            if (l.isEditable()) {
                m_fab.show();
            } else {
                m_fab.hide();
            }
        }

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

        return null;
    }

    private Level getLevelInfo(Long p_id) {
        Long l_id = getCurrentLevel(p_id);
        for (Level l : m_levels) {
            if (l.getId().equals(l_id)) {
                return l;
            }
        }
        return null;
    }

    private Long getCurrentLevel(Long p_id) {
        if (p_id == null)
            return 0L;

        for (Plan p : m_viewModel.getAllPlans()) {
            if (p_id.equals(p.getId())) {
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