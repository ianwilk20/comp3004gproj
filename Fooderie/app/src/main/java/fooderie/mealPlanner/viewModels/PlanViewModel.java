package fooderie.mealPlanner.viewModels;

import android.app.Application;

import java.lang.reflect.Method;
import java.security.acl.Owner;
import java.util.List;
import java.util.function.Function;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.models.FooderieRepository;

public class PlanViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;

    public PlanViewModel (Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
    }

    /* Entity=Plan, repository interactions */
    public void insert(Plan plan) { m_repo.insert(plan); }
    public void deleteAllPlans() {m_repo.deleteAllPlans();}
    public void deletePlan(Long id) {m_repo.deletePlan(id);}

    public LiveData<List<Plan>> getChildrenOfPlan(Long i) {return m_repo.getChildrenOfPlan(i); }

    /* Entity=PlanRecipe, repository interactions */
    public void insert(PlanRecipe planRecipe) {m_repo.insert(planRecipe);}

    /* Helper Functions */

}
