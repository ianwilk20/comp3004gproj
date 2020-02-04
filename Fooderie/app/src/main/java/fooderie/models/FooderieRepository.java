package fooderie.models;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanRecipe;

public class FooderieRepository {
    private FooderieDao fooderieDao;

    public FooderieRepository(Application application) {
        FooderieRoomDatabase db = FooderieRoomDatabase.getDatabase(application);
        fooderieDao = db.fooderieDao();
    }

    /* Entity=Plan, repository interactions */
    public void insert(Plan p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.insert(p));
    }

    public List<Plan> getChildrenOfPlan(int id) {
        return fooderieDao.getChildrenOfPlan(id);
    }
    public LiveData<List<Plan>> getWeeklyMealPlans() {
        return fooderieDao.getWeeklyMealPlans();
    }

    /* Entity=PlanRecipe, repository interactions */
    public void insert(PlanRecipe pr) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.insert(pr));
    }
}
