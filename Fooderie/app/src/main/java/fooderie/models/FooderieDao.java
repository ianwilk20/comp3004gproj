package fooderie.models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;

@Dao
public interface FooderieDao {

    /* Entity=PlanWeek, PlanDay, PlanMeal, PlanRecipe, dao interactions */
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

    @Delete
    void delete(PlanWeek p);
    @Delete
    void delete(PlanDay p);
    @Delete
    void delete(PlanMeal p);
    @Delete
    void delete(PlanRecipe p);

    @Query("SELECT * FROM table_PlanWeek")
    LiveData<List<PlanWeek>>  getWeekPlans();

    @Query("SELECT * FROM table_PlanDay WHERE parentId == :id")
    LiveData<List<PlanDay>>  getDayPlans(Long id);

    @Query("SELECT * FROM table_PlanMeal WHERE parentId == :id")
    LiveData<List<PlanMeal>>  getMealPlans(Long id);

    @Query("SELECT * FROM table_PlanRecipe WHERE parentId == :id")
    LiveData<List<PlanRecipe>>  getRecipePlans(Long id);

    //@Query("SELECT table_Recipe.recipe_id FROM table_PlanRecipe, table_Recipe WHERE table_PlanRecipe.plan_id == :id AND table_PlanRecipe.recipe_id == table_Recipe.recipe_id")
    //List<Recipe> getRecipesForPlan(Long id);
}
