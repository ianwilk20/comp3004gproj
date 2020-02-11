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
import fooderie.mealPlanner.models.Depth;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.viewModels.PlanViewModel;


import com.example.fooderie.R;

import java.util.List;
import java.util.stream.Collectors;

public class PlanRecyclerView extends AppCompatActivity {
    private final String m_TAG = PlanRecyclerView.class.getSimpleName();

    private PlanViewModel m_viewModel;
    private FloatingActionButton m_fab;

    private Plan m_parentPlan;
    private PlanAdapter m_adaptor;

    private final Plan ROOT = new Plan(null, null, "ROOT", -1,-1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_adaptor = new PlanAdapter(this, this::displayChildrenPlansOfID, this::deletePlan);
        RecyclerView recyclerView = findViewById(R.id.PlanRecyclerView);
        recyclerView.setAdapter(m_adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_parentPlan = ROOT;

        m_fab = findViewById(R.id.fab);
        m_fab.setOnClickListener((View view) -> addPlanDialog());

        m_viewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        displayChildrenPlansOfID(m_parentPlan);
        //m_viewModel.getChildrenOfPlan(m_parentId).observe(this, this::displayPlans);



        // -- Pre-populate Database -- //
        //m_viewModel.deleteAllPlans();
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS A TEST"));
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS NOT A TEST"));
        //m_viewModel.insert(new Plan(m_parentId, "THIS IS MAYBE A TEST"));
    }

    private void addPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Depth.getAddDialogText(m_parentPlan));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
            String planName = input.getText().toString();
            Plan p = new Plan(m_parentPlan.getId(), planName, m_parentPlan.getDepth()+1, 0);

            m_viewModel.insert(p);
        }));
        builder.setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> dialog.dismiss()));

        builder.show();
    }

    private Void deletePlan(Plan p) {
        m_viewModel.deletePlan(p.getId());
        return null;
    }

    private Void displayChildrenPlansOfID(Plan p) {
        // -- REMOVE OLD LIVEDATA OBSERVERS -- //
        m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).removeObservers(this);
        Log.d("BANANA", "displayChildrenPlansOfID:" + m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).hasActiveObservers());
        // -- MAKE NEW OBSERVER FOR THE CHILDREN OF THE CURRENT PARENT PLAN -- //
        m_parentPlan = (p == null) ? ROOT : p;
        m_viewModel.getChildrenOfPlan(m_parentPlan.getId()).observe(this, this::displayPlans);

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
    }
}