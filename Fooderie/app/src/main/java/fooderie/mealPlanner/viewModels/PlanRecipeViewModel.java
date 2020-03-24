package fooderie.mealPlanner.viewModels;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie._main.models.FooderieRepository;
import fooderie.recipeBrowser.models.Recipe;

public class PlanRecipeViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;

    public PlanRecipeViewModel(Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
    }

    public void setupDisplay(Plan p, LifecycleOwner owner, Observer o) {
        p.setLiveData(m_repo, owner, o);
    }

    public void updatePlanMealsOrder(LifecycleOwner owner, Long id, List<Pair<Integer, Integer>> moves) {
        LiveData<List<PlanMeal>> liveData = m_repo.getMealPlans(id);
        Observer<List<PlanMeal>> o = new Observer<List<PlanMeal>>() {
            @Override
            public void onChanged(@Nullable List<PlanMeal> plans) {
                if (plans == null)
                    return;

                for (Pair<Integer, Integer> m : moves) {
                    if (m.first == null || m.second == null) continue;
                    PlanMeal p1 = (PlanMeal) plans.stream().filter(p -> m.first.equals(p.getOrder())).toArray()[0];
                    PlanMeal p2 = (PlanMeal) plans.stream().filter(p -> m.second.equals(p.getOrder())).toArray()[0];

                    int tmp = p1.getOrder();
                    int tmpHour = p1.getHour();
                    int tmpMinute = p1.getMinute();
                    p1.setOrder(p2.getOrder());
                    p1.setHour(p2.getHour());
                    p1.setMinute(p2.getMinute());
                    p2.setOrder(tmp);
                    p2.setHour(tmpHour);
                    p2.setMinute(tmpMinute);
                }

                m_repo.update(plans);
                liveData.removeObserver(this);
            }
        };
        liveData.observe(owner, o);
    }

    public void deletePlanRecipe(Long p_id, String r_id) {
        m_repo.deletePlanRecipe(p_id, r_id);
    }

    // -- Makes new recipe entity in database, and makes new link to it -- //
    public void insertRecipeAndPlanRecipe(Long p_id, Recipe r) {
        m_repo.insert(p_id, r);
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
        } else if (p instanceof PlanMeal) {
            m_repo.delete((PlanMeal) p);
        } else {
            Log.d("BANANA", "insertPlan: Plan was not deleted. "+ p.toString());
        }
    }
}
