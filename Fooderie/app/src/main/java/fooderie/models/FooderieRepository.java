package fooderie.models;

import android.app.Application;
import android.util.Log;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;

public class FooderieRepository {
    private FooderieDao fooderieDao;

    public FooderieRepository(Application application) {
        FooderieRoomDatabase db = FooderieRoomDatabase.getDatabase(application);
        fooderieDao = db.fooderieDao();
    }

    /* Entity=PlanWeek, PlanDay, PlanMeal, PlanRecipe, repository interactions */
    public void insert(PlanWeek p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            long id = fooderieDao.insert(p);
            List<PlanDay> plans = new ArrayList<PlanDay>();
            for (DayOfWeek d : DayOfWeek.values()) {
                PlanDay day = new PlanDay((Long) id, d.toString(), p.getRecipeCount());
                plans.add(day);
            }
            fooderieDao.insert(plans);
        });
    }
    public void insert(PlanDay p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.insert(p));
    }
    public void insert(PlanMeal p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.insert(p));
    }
    public void insert(PlanRecipe p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.insert(p));
    }

    public void delete(PlanWeek p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(p));
    }
    public void delete(PlanDay p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(p));
    }
    public void delete(PlanMeal p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(p));
    }
    public void delete(PlanRecipe p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(p));
    }

    public LiveData<List<PlanWeek>> getWeekPlans() { return fooderieDao.getWeekPlans(); }
    public LiveData<List<PlanDay>> getDayPlans(Long id) { return fooderieDao.getDayPlans(id); }
    public LiveData<List<PlanMeal>> getMealPlans(Long id) { return fooderieDao.getMealPlans(id); }
    public LiveData<List<PlanRecipe>> getRecipePlans(Long id) { return fooderieDao.getRecipePlans(id); }
}
