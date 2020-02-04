package fooderie.mealPlanner.viewModels;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.models.FooderieRepository;

public class PlanViewModel extends AndroidViewModel {
    private FooderieRepository repo;

    public PlanViewModel (Application application) {
        super(application);
        repo = new FooderieRepository(application);
    }

    public void insert(Plan plan) {repo.insert(plan);}
    public LiveData<List<Plan>> getWeeklyMealPlans() {return repo.getWeeklyMealPlans(); }

    public void insert(PlanRecipe planRecipe) {repo.insert(planRecipe);}
}
