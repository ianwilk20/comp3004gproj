package fooderie.models;

import android.app.Application;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import fooderie.groceryList.models.Food;
import fooderie.groceryList.models.UserGroceryListItem;
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
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.insert(p);
            updatePlanMealRecipeCount(p.getParentId(), 1);
        });
    }
    public void insert(Food f){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.insert(f);
        });
    }
    public void insert(UserGroceryListItem item){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.insert(item);
        });
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
    //Necessary? To update food if only populated once
    public void update(Food f){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(f);
        });
    }
    public void update(UserGroceryListItem item){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(item);
        });
    }
    public void updateGroceryItemAttributes(String prevName, String newName, String quantity, String notes, String department){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.updateGroceryItemAttributes(prevName, newName, quantity, notes, department);
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
    public void delete(Food f){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(f));
    }
    public void delete(UserGroceryListItem item){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.delete(item));
    }
    public void deleteAllFoodFromAPI(){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.deleteAllFoodFromAPI());
    }
    public void deleteAllGroceryItems(){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.deleteAllGroceryItems());
    }
    public void deleteGroceryItemByName(String ingredientName){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> fooderieDao.deleteGroceryItemByName(ingredientName));
    }
    public void deletePlanRecipe(Long p_id, Long r_id) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            PlanRecipe pr = fooderieDao.getPlanRecipe(p_id, r_id);
            fooderieDao.delete(pr);

            updatePlanMealRecipeCount(pr.getParentId(), -1);
        });
    }

    public LiveData<List<PlanWeek>> getWeekPlans() { return fooderieDao.getWeekPlans(); }
    public LiveData<List<PlanDay>> getDayPlans(Long id) { return fooderieDao.getDayPlans(id); }
    public LiveData<List<PlanMeal>> getMealPlans(Long id) { return fooderieDao.getMealPlans(id); }
    public LiveData<List<Recipe>> getRecipes(Long id) { return fooderieDao.getRecipes(id); }
    public LiveData<List<Food>> getAllAPIIngredients() { return  fooderieDao.getAllAPIIngredients(); }
    public LiveData<Food> getFoodByID(String food_id) { return fooderieDao.getFoodByID(food_id); }
    public LiveData<List<Food>> getFoodByLabel(String label) {return fooderieDao.getFoodByLabel(label); }
    public LiveData<List<UserGroceryListItem>> getAllGroceryItems() { return fooderieDao.getAllGroceryItems(); }

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
