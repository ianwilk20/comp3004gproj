package fooderie.models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanRecipe;

@Dao
public interface FooderieDao {

    /* Entity=Plan, dao interactions */
    @Insert
    void insert(Plan plan);

    @Query("DELETE FROM table_Plan")
    void deleteAllPlans();

    @Query("SELECT * FROM table_Plan WHERE parent_id == :id")
    List<Plan> getChildrenOfPlan(int id);

    @Query("SELECT * FROM table_Plan WHERE parent_id IS NULL")
    LiveData<List<Plan>>  getWeeklyMealPlans();


    /* Entity=PlanRecipe, dao interactions */
    @Insert
    void insert(PlanRecipe planRecipe);

    @Query("DELETE FROM table_PlanRecipe")
    void deleteAllPlanRecipes();

    @Query("SELECT table_Recipe.recipe_id FROM table_PlanRecipe, table_Recipe WHERE table_PlanRecipe.plan_id == :id AND table_PlanRecipe.recipe_id == table_Recipe.recipe_id")
    List<Recipe> getRecipesForPlan(int id);
}
