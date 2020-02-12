package fooderie.mealPlanner.viewModels;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Method;
import java.security.acl.Owner;
import java.util.List;
import java.util.function.Function;

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

    public void deletePlan(Plan p) {
        if (p instanceof PlanWeek) {
            m_repo.delete((PlanWeek) p);
        } else if (p instanceof PlanDay) {
            m_repo.delete((PlanDay) p);
        } else if (p instanceof PlanMeal) {
            m_repo.delete((PlanMeal) p);
        } else if (p instanceof PlanRecipe) {
            m_repo.delete((PlanRecipe) p);
        } else {
            Log.d("BANANA", "insertPlan: Plan was not deleted. "+ p.toString());
        }
    }

    public void insertPlan(Plan p) {
        if (p instanceof PlanWeek) {
            m_repo.insert((PlanWeek) p);
        } else if (p instanceof PlanDay) {
            m_repo.insert((PlanDay) p);
        } else if (p instanceof PlanMeal) {
            m_repo.insert((PlanMeal) p);
        } else if (p instanceof PlanRecipe) {
            m_repo.insert((PlanRecipe) p);
        } else {
            Log.d("BANANA", "insertPlan: Plan was not inserted. "+ p.toString());
        }
    }
}
