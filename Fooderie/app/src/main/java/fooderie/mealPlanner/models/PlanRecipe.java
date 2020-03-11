package fooderie.mealPlanner.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fooderie.recipeBrowser.models.Recipe;

@Entity(tableName = "table_PlanRecipe",
        indices = {@Index("planId"), @Index("parentId"), @Index("recipeId")},
        foreignKeys = {
            @ForeignKey(entity = PlanMeal.class,
                parentColumns = "planId",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Recipe.class,
                parentColumns = "recipe_id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE)
        })
public class PlanRecipe {
    @PrimaryKey (autoGenerate = true)
    private Long planId;
    private Long parentId;
    private String recipeId;

    @Ignore
    private static final String planName = "Recipe";
    @Ignore
    static final PropertiesForPlan properties = new PropertiesForPlan(true, false, false, planName);

    public Long getPlanId() {
        return planId;
    }
    public void setPlanId(Long planId) {
        this.planId = planId;
    }
    public Long getParentId() {
        return parentId;
    }
    public String getRecipeId() {
        return recipeId;
    }

    public PlanRecipe(Long parentId, String recipeId) {
        this.parentId = parentId;
        this.recipeId = recipeId;
    }
}