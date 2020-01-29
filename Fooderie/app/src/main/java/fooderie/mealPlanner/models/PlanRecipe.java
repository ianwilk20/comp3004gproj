package fooderie.mealPlanner.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import fooderie.models.Recipe;

@Entity(tableName = "table_PlanRecipe",
        foreignKeys = {
            @ForeignKey(entity = Plan.class,
                parentColumns = "id",
                childColumns = "planId",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE)
        })
public class PlanRecipe {
    @PrimaryKey
    private int id;
    private int planId;
    private int recipeId;

    public int getId() {return id;}
    public int getPlanId() {return planId;}
    public int getRecipeId() {return recipeId;}
}