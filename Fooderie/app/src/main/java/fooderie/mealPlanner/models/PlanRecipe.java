package fooderie.mealPlanner.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fooderie.models.Recipe;

@Entity(tableName = "table_PlanRecipe",
        indices = {@Index("join_plan_recipe_id"), @Index("plan_id"), @Index("recipe_id")},
        foreignKeys = {
            @ForeignKey(entity = Plan.class,
                parentColumns = "plan_id",
                childColumns = "plan_id",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Recipe.class,
                parentColumns = "recipe_id",
                childColumns = "recipe_id",
                onDelete = ForeignKey.CASCADE)
        })
public class PlanRecipe {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="join_plan_recipe_id")
    private int id;
    @ColumnInfo(name="plan_id")
    private int planId;
    @ColumnInfo(name="recipe_id")
    private int recipeId;

    public PlanRecipe(int planId, int recipeId) {
        this.planId = planId;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getPlanId() {return planId;}
    public int getRecipeId() {return recipeId;}
}