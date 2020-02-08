package fooderie.models;

import android.app.Application;
import android.util.Log;

import java.time.DayOfWeek;
import java.util.ArrayList;
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
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            long p_id = fooderieDao.insert(p);
            Log.d("BANANA", "insert: plan:"+p.toString()+", Long:"+Long.toString(p_id));

            if (p.getParentId() == null) {
                //List<Plan> plans = new ArrayList<Plan>();
                for (DayOfWeek d : DayOfWeek.values()) {
                    Plan day = new Plan(p_id, d.toString());
                    //plans.add(day);
                    Log.d("BANANA2", "insert: plan:"+day.toString());
                    long c_id = fooderieDao.insert(day);
                }
                //fooderieDao.insert(day);
            }
        });
    }

    public void insert(List<Plan> plans) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> { fooderieDao.insert(plans); });
    }

    public void deleteAllPlans() {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.deleteAllPlans());
    }

    public LiveData<List<Plan>> getAllPlans() {
        return fooderieDao.getAllPlans();
    }
    public LiveData<List<Plan>> getChildrenOfPlan(int id) {
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
