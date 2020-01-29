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

    @Query("SELECT * FROM table_Plan WHERE parentId == :id")
    List<Plan> getChildrenOfPlan(int id);

    @Query("SELECT * FROM table_Plan WHERE parentId == 0")
    LiveData<List<Plan>>  getWeeklyMealPlans();


    /* Entity=PlanRecipe, dao interactions */
    @Insert
    void insert(PlanRecipe planRecipe);

    @Query("DELETE FROM table_PlanRecipe")
    void deleteAllPlanRecipes();

    @Query("SELECT * FROM table_PlanRecipe, table_Recipe WHERE planId == :id AND table_Recipe.id == recipeId")
    List<Recipe> getRecipesForPlan(int id);
}
