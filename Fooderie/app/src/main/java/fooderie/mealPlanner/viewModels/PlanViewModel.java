package fooderie.mealPlanner.viewModels;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Method;
import java.security.acl.Owner;
import java.util.List;
import java.util.function.Function;

import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.models.FooderieRepository;
import fooderie.models.Recipe;

public class PlanViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;

    public PlanViewModel (Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
    }

    public void setupDisplay(Plan p, LifecycleOwner owner, Observer o) {
        p.removeLiveData(owner);
        p.setLiveData(m_repo, owner, o);
    }

    public void updatePlanMealsOrder(Long id, List<Pair<Integer, Integer>> moves) {
        m_repo.updatePlanMealsOrder(id, moves);
    }

    public void insertPlanRecipe(PlanRecipe pr) {
        m_repo.insert(pr);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return m_repo.getAllRecipes();
    }
    public LiveData<List<PlanWeek>> getAllWeeklyMealPlans() {
        return m_repo.getWeekPlans();
    }

    public void insertPlan(Plan p) {
        if (p instanceof PlanWeek) {
            m_repo.insert((PlanWeek) p);
        } else if (p instanceof PlanDay) {
            m_repo.insert((PlanDay) p);
        } else if (p instanceof PlanMeal) {
            m_repo.insert((PlanMeal) p);
        } else {
            Log.d("BANANA", "insertPlan: Plan was not inserted. "+ p.toString());
        }
    }

    public void updatePlan(Plan p) {
        if (p instanceof PlanWeek) {
            m_repo.update((PlanWeek) p);
        } else if (p instanceof PlanDay) {
            m_repo.update((PlanDay) p);
        } else if (p instanceof PlanMeal) {
            m_repo.update((PlanMeal) p);
        } else {
            Log.d("BANANA", "updatePlan: Plan was not updated. "+ p.toString());
        }
    }

    public void deletePlan(Plan p) {
        if (p instanceof PlanWeek) {
            m_repo.delete((PlanWeek) p);
        } else if (p instanceof PlanDay) {
            m_repo.delete((PlanDay) p);
        } else if (p instanceof PlanMeal) {
            m_repo.delete((PlanMeal) p);
        } else {
            Log.d("BANANA", "insertPlan: Plan was not deleted. "+ p.toString());
        }
    }
}
