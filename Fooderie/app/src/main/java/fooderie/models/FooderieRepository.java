package fooderie.models;

import android.app.Application;
import android.util.Log;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Pair;
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
            List<PlanDay> plans = new ArrayList<>();
            for (DayOfWeek d : DayOfWeek.values()) {
                PlanDay day = new PlanDay(id, d.toString(), p.getRecipeCount());
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
        FooderieRoomDatabase.databaseWriteExecutor.execute(() ->
            insertPlanRecipe(p)
        );
    }
    public void insert(Long p_id, Recipe r) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.insert(r);
            insertPlanRecipe(new PlanRecipe(p_id, r.getId()));
        });
    }
    private void insertPlanRecipe(PlanRecipe p) {
        fooderieDao.insert(p);
        updatePlanMealRecipeCount(p.getParentId(), 1);
    }

    public void update(PlanWeek p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(p);
        });
    }
    public void update(PlanDay p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(p);
        });
    }
    public void update(PlanMeal p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(p);
        });
    }
    public void update(PlanRecipe p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(p);
        });
    }
    public void updatePlanMealsOrder(Long id, List<Pair<Integer, Integer>> moves) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<PlanMeal> plans = fooderieDao.getAllMealPlans(id);

            for (Pair<Integer, Integer> m : moves) {
                if (m.first == null || m.second == null) continue;
                PlanMeal p1 = (PlanMeal) plans.stream().filter(p -> m.first.equals(p.getOrder())).toArray()[0];
                PlanMeal p2 = (PlanMeal) plans.stream().filter(p -> m.second.equals(p.getOrder())).toArray()[0];

                int tmp = p1.getOrder();
                p1.setOrder(p2.getOrder());
                p2.setOrder(tmp);
            }

            for (PlanMeal p : plans) {
                fooderieDao.update(p);
            }
        });
    }

    public void delete(PlanWeek p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(p));
    }
    public void delete(PlanMeal p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            PlanMeal pm = fooderieDao.getPlanMeal(p.getPlanId());
            fooderieDao.delete(pm);

            updatePlanDayRecipeCount(pm.getParentId(), pm.getRecipeCount() * -1);
        });
    }
    public void deletePlanRecipe(Long p_id, String r_id) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            // -- Get and delete the PlanRecipe -- //
            PlanRecipe pr = fooderieDao.getPlanRecipe(p_id, r_id);
            fooderieDao.delete(pr);

            // -- Get and delete the Recipe -- //
            Recipe r = fooderieDao.getRecipe(r_id);
            fooderieDao.delete(r);

            // -- Update the recipe count of all parents -- //
            updatePlanMealRecipeCount(pr.getParentId(), -1);
        });
    }

    public LiveData<List<PlanWeek>> getWeekPlans() { return fooderieDao.getWeekPlans(); }
    public LiveData<List<PlanDay>> getDayPlans(Long id) { return fooderieDao.getDayPlans(id); }
    public LiveData<List<PlanMeal>> getMealPlans(Long id) { return fooderieDao.getMealPlans(id); }
    public LiveData<List<Recipe>> getRecipes(Long id) { return fooderieDao.getRecipes(id); }

    public LiveData<List<Recipe>> getAllRecipesFromWeeklyMealPlanId(Long id) { return fooderieDao.getAllRecipesFromWeeklyMealPlanId(id); }

    private void updatePlanMealRecipeCount(Long id, int change) {
        PlanMeal pm = fooderieDao.getPlanMeal(id);
        pm.changeCount(change);
        update(pm);

        updatePlanDayRecipeCount(pm.getParentId(), change);
    }
    private void updatePlanDayRecipeCount(Long id, int change) {
        PlanDay pd = fooderieDao.getPlanDay(id);
        pd.changeCount(change);
        update(pd);

        updatePlanWeekRecipeCount(pd.getParentId(), change);
    }
    private void updatePlanWeekRecipeCount(Long id, int change) {
        PlanWeek pw = fooderieDao.getPlanWeek(id);
        pw.changeCount(change);
        update(pw);
    }
}
