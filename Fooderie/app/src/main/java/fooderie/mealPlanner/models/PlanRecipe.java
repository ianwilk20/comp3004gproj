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
    private Long id;
    @ColumnInfo(name="plan_id")
    private Long planId;
    @ColumnInfo(name="recipe_id")
    private Long recipeId;

    public PlanRecipe(Long planId, Long recipeId) {
        this.planId = planId;
        this.recipeId = recipeId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {return planId;}
    public Long getRecipeId() {return recipeId;}
}