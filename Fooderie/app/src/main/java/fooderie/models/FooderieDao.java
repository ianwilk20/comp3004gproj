package fooderie.models;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import androidx.room.RoomWarnings;
import androidx.room.Update;

import fooderie.groceryList.models.Food;
import fooderie.groceryList.models.UserGroceryListItem;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.recipeBrowser.models.Recipe;
import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FooderieDao {

    /* Entity=Schedule */
    @Insert
    long insert(Schedule s);

    @Update
    void update(Schedule s);

    @Delete
    void delete(Schedule s);
    @Query("DELETE FROM table_Schedule")
    void deleteAllSchedules();

    @Query("SELECT * FROM table_Schedule")
    LiveData<List<Schedule>> getAllSchedules();
    @Query("SELECT * FROM table_Schedule")
    List<Schedule> getAllSchedulesNonLiveData();

    /* Entity=PlanWeek, PlanDay, PlanMeal, PlanRecipe dao interactions */
    @Insert
    long insert(PlanWeek p);
    @Insert
    long insert(PlanDay p);
    @Insert
    List<Long> insert(List<PlanDay> p);
    @Insert
    long insert(PlanMeal p);
    @Insert
    long insert(PlanRecipe p);

    @Update
    void update(PlanWeek p);
    @Update
    void update(PlanDay p);
    @Update
    void update(PlanMeal p);
    @Update
    void update(PlanRecipe p);

    @Delete
    void delete(PlanWeek p);
    @Delete
    void delete(PlanDay p);
    @Delete
    void delete(PlanMeal p);
    @Delete
    void delete(PlanRecipe p);

    @Query("SELECT * FROM table_PlanWeek")
    LiveData<List<PlanWeek>> getPlanWeeks();
    @Query("SELECT * FROM table_PlanWeek")
    List<PlanWeek> getAllPlanWeeks();
    @Query("SELECT * FROM table_PlanWeek WHERE planId == :id")
    PlanWeek getPlanWeek(Long id);

    @Query("SELECT * FROM table_PlanDay WHERE parentId == :id")
    LiveData<List<PlanDay>> getDayPlans(Long id);
    @Query("SELECT * FROM table_PlanDay WHERE planId == :id")
    PlanDay getPlanDay(Long id);

    @Query("SELECT * FROM table_PlanMeal WHERE parentId == :id")
    LiveData<List<PlanMeal>> getMealPlans(Long id);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_Schedule s, table_PlanWeek pw, table_PlanDay pd, table_PlanMeal pm " +
           "WHERE s.weekOfYearId == :weekNum AND s.planId == pw.planId AND pd.parentId == pw.planId AND pd.name LIKE :dayName " +
           "AND pm.parentId == pd.planId ORDER BY pm.'order'")
    LiveData<List<PlanMeal>> getMealPlans(Long weekNum, String dayName);
    @Query("SELECT * FROM table_PlanMeal WHERE parentId == :id")
    List<PlanMeal> getAllMealPlans(Long id);
    @Query("SELECT * FROM table_PlanMeal WHERE planId == :id")
    PlanMeal getPlanMeal(Long id);

    @Query ("DELETE FROM table_PlanRecipe")
    void deleteAllPlanRecipes();
    @Query("SELECT * FROM table_PlanRecipe WHERE parentId == :p_id AND recipeId == :r_id")
    PlanRecipe getPlanRecipe(Long p_id, String r_id);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_PlanRecipe pr, table_Recipe r WHERE pr.parentId == :id AND pr.recipeId == r.recipe_id")
    LiveData<List<Recipe>> getRecipes(Long id);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_Schedule s, table_PlanWeek pw, table_PlanDay pd, table_PlanMeal pm, table_PlanRecipe pr, table_Recipe r " +
            "WHERE s.weekOfYearId == :weekNum AND s.planId == pw.planId AND pd.parentId == pw.planId AND pm.parentId == pd.planId " +
            "AND pr.parentId == pm.planId AND r.recipe_id == pr.recipeId")
    LiveData<List<Recipe>> getNextWeeksRecipes(Long weekNum);

    /* Entity=Recipe dao interactions */
    @Insert
    void insert(Recipe r);
    @Delete
    void delete(Recipe r);

    @Query("DELETE FROM table_Recipe")
    void deleteAllRecipes();

    @Query("SELECT * FROM table_Recipe")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM table_Recipe WHERE favorite ==" + true)
    List<Recipe> getAllFavs();

    @Query("SELECT * FROM table_Recipe WHERE recipe_id == :url")
    Recipe getRecipe(String url);

    @Query("SELECT * FROM table_Recipe WHERE recipe_id == :url AND favorite ==" + true)
    Recipe getFav(String url);

    /* Entity=Food dao interactions */
    @Insert(onConflict = REPLACE)
    void insert(Food f);
    @Update
    void update(Food f);
    @Delete
    void delete(Food f);

    @Query("DELETE FROM table_APIIngredient")
    void deleteAllFoodFromAPI();

    @Query("SELECT * FROM table_APIIngredient")
    LiveData<List<Food>> getAllAPIIngredients();

    @Query("SELECT * FROM table_APIIngredient " +
            "WHERE table_APIIngredient.food_id = :food_id")
    LiveData<Food> getFoodByID(String food_id);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_APIIngredient " +
            "WHERE table_APIIngredient.label LIKE '%' || :label || '%'")
    List<Food> getFoodByLabel(String label); //MUST enter label as "apple%" to get all results with apples

    /* Entity=UserGroceryListItem dao interactions */
    @Insert(onConflict = REPLACE)
    void insert(UserGroceryListItem item);
    @Update
    void update(UserGroceryListItem item);
    @Delete
    void delete(UserGroceryListItem item);

    @Query("DELETE FROM table_userGroceryList")
    void deleteAllGroceryItems();

    @Query("DELETE FROM table_userGroceryList " +
            "WHERE table_userGroceryList.food_name = :ingredientName")
    void deleteGroceryItemByName(String ingredientName);

    @Query("SELECT * FROM table_userGroceryList")
    LiveData<List<UserGroceryListItem>> getAllGroceryItems();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_userGroceryList " +
            "WHERE table_userGroceryList.inPantry = 1")
    List<UserGroceryListItem> getItemsInPantry();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_userGroceryList " +
            "WHERE table_userGroceryList.food_name LIKE '%' || :food_name || '%'")
    List<UserGroceryListItem> getItemInGroceryList(String food_name);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE table_userGroceryList " +
            "SET food_name = :newName, quantity = :quantity, notes = :notes, department = :department, inPantry = :inPantry " +
            "WHERE table_userGroceryList.food_id = :food_id")
    void updateGroceryItemAttributes(String food_id, String newName, String quantity, String notes, String department, boolean inPantry);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE table_userGroceryList " +
            "SET inPantry = :inPantry " +
            "WHERE table_userGroceryList.food_id = :foodId")
    void updateInPantryStatus(String foodId, boolean inPantry);

}
