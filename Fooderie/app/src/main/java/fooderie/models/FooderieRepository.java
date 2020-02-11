package fooderie.models;

import android.app.Application;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.Depth;
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
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            long p_id = fooderieDao.insert(p);

            // -- Week Plans Must Be PrePopulated With the Seven day Week -- //
            if (Depth.isAtDepth(p, Depth.WEEK_PLAN)) {
                List<Plan> plans = new ArrayList<Plan>();
                for (DayOfWeek d : DayOfWeek.values()) {
                    Plan c = new Plan(p_id, d.toString(), Depth.getId(Depth.DAY_PLAN),0);
                    plans.add(c);
                }
                insert(plans);
            }
        });
    }

    public void insert(List<Plan> plans) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> { fooderieDao.insert(plans); });
    }

    public void deleteAllPlans() {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.deleteAllPlans());
    }
    public void deletePlan(Long id) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.deletePlan(id));
    }

    public LiveData<List<Plan>> getAllPlans() {
        return fooderieDao.getAllPlans();
    }
    public LiveData<List<Plan>> getChildrenOfPlan(Long id) {
        if (id == null)
            return fooderieDao.getWeeklyMealPlans();
        else
            return fooderieDao.getChildrenOfPlan(id);
    }

    /* Entity=PlanRecipe, repository interactions */
    public void insert(PlanRecipe pr) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.insert(pr));
    }
}
