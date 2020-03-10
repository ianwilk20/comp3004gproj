package fooderie.models;

import android.app.Application;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import fooderie.groceryList.models.Food;
import fooderie.groceryList.models.UserGroceryListItem;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.mealPlannerScheduler.models.ScheduleAndPlanWeek;
import fooderie.recipeBrowser.models.Recipe;


public class FooderieRepository {
    private FooderieDao fooderieDao;

    public FooderieRepository(Application application) {
        FooderieRoomDatabase db = FooderieRoomDatabase.getDatabase(application);
        fooderieDao = db.fooderieDao();
    }

    /* Entity=Schedule */
    public LiveData<List<ScheduleAndPlanWeek>> getAllSchedules() {
        return fooderieDao.getAllSchedules();
    }
    public void update(Schedule s) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(s);
        });
    }

    public LiveData<List<Recipe>> getNextWeeksRecipes() {
        Calendar c = Calendar.getInstance();
        Long week = (Long) Integer.toUnsignedLong((c.get(Calendar.WEEK_OF_YEAR )+ 1) % 53);
        return fooderieDao.getNextWeeksRecipes(week);
    }

    /* Entity=PlanWeek, PlanDay, PlanMeal, PlanRecipe, repository interactions */
    public void insert(PlanWeek p) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            long id = fooderieDao.insert(p);
            List<PlanDay> plans = new ArrayList<>();

            int offset = 6; //Make sure the week starts on the sunday
            DayOfWeek[] days = DayOfWeek.values();
            for (int i = 0; i < 7; i++) {
                DayOfWeek day = days[(i + offset) % 7];
                PlanDay pd = new PlanDay(id, day.toString(), p.getRecipeCount());
                plans.add(pd);
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
            Recipe tmp = fooderieDao.getRecipe(r.getId());
            if (tmp == null)
                fooderieDao.insert(r);
            insertPlanRecipe(new PlanRecipe(p_id, r.getId()));
        });
    }

    private void insertPlanRecipe(PlanRecipe p) {
        fooderieDao.insert(p);
        updatePlanMealRecipeCount(p.getParentId(), 1);
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
    public void update(List<PlanMeal> plans) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.update(plans);
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
    public void updateGroceryItemAttributes(String prevName, String newName, String quantity, String notes, String department, boolean inPantry){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.updateGroceryItemAttributes(prevName, newName, quantity, notes, department, inPantry);
        });
    }
    public void updateInPantryStatus(String foodId, boolean inPantry){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            fooderieDao.updateInPantryStatus(foodId, inPantry);
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
    public void deletePlanRecipe(Long p_id, String r_id) {
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {
            // -- Get and delete the PlanRecipe -- //
            PlanRecipe pr = fooderieDao.getPlanRecipe(p_id, r_id);
            fooderieDao.delete(pr);

            // -- Update the recipe count of all parents -- //
            updatePlanMealRecipeCount(pr.getParentId(), -1);
        });
    }

    public LiveData<List<PlanWeek>> getWeekPlans() { return fooderieDao.getPlanWeeks(); }
    public LiveData<List<PlanDay>> getDayPlans(Long id) { return fooderieDao.getDayPlans(id); }
    public LiveData<List<PlanMeal>> getMealPlans(Long id) { return fooderieDao.getMealPlans(id); }
    public LiveData<List<PlanMeal>> getMealPlans(Long weekNum, String dayName) {
        return fooderieDao.getMealPlans(weekNum, dayName);
    }
    public LiveData<List<Recipe>> getRecipes(Long id) { return fooderieDao.getRecipes(id); }
    public LiveData<List<Food>> getAllAPIIngredients() { return  fooderieDao.getAllAPIIngredients(); }
    public LiveData<Food> getFoodByID(String food_id) { return fooderieDao.getFoodByID(food_id); }
    public LiveData<List<Food>> getFoodByLabel(String label) {return fooderieDao.getFoodByLabel(label); }
    public LiveData<List<UserGroceryListItem>> getAllGroceryItems() { return fooderieDao.getAllGroceryItems(); }
    public LiveData<List<UserGroceryListItem>> getItemsInPantry() {return fooderieDao.getItemsInPantry(); }

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

    //Recipe Browser Stuff
    public void insert(Recipe r){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {fooderieDao.insert(r);});
    }
    public void delete(Recipe r){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {fooderieDao.delete(r);});
    }
    public void deleteAllRecipes(){
        FooderieRoomDatabase.databaseWriteExecutor.execute(() -> {fooderieDao.deleteAllRecipes();});
    }
    public List<Recipe> getAllRecipes(){return fooderieDao.getAllRecipes();}
    public List<Recipe> getAllFavs(){return fooderieDao.getAllFavs();}
    public Recipe getRecipe(String url){return fooderieDao.getRecipe(url);}
    public Recipe getFav(String url){return fooderieDao.getFav(url);}
}
