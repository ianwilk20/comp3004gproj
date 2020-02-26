package fooderie.models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import androidx.room.RoomWarnings;
import androidx.room.Update;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlannerScheduler.models.Schedule;

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
    @Query("SELECT * FROM table_PlanWeek w, table_PlanDay d, table_PlanMeal m, table_PlanRecipe pr, table_Recipe r " +
            "WHERE w.planId == :id AND d.parentId == w.planId AND m.parentId == d.planId AND pr.parentId == m.planId " +
            "AND pr.recipeId == r.recipe_id")
    LiveData<List<Recipe>> getAllRecipesFromWeeklyMealPlanId(Long id);


    /* Entity=Recipe dao interactions */
    @Insert
    void insert(Recipe r);
    @Delete
    void delete(Recipe r);

    @Query("DELETE FROM table_Recipe")
    void deleteAllRecipes();

    @Query("SELECT * FROM table_Recipe")
    List<Recipe> getAllRecipes();
    @Query("SELECT * FROM table_Recipe WHERE recipe_id == :id")
    Recipe getRecipe(String id);
}
