package fooderie.mealPlanner.models;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fooderie.models.FooderieRepository;
import fooderie.models.Recipe;

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
public class PlanRecipe extends Plan{
    private Long recipeId;
    public Long getRecipeId() {return recipeId;}

    public PlanRecipe(Long parentId, String name, int recipeCount, Long recipeId) {
        super(parentId, name, recipeCount);
        this.recipeId = recipeId;
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        // -- TODO: Change to viewing recipes -- //
        //children = repo.getDayPlans(planId);
        //children.observe(owner, o);
    }

    @Override
    public Plan makeChild(Long parentId, String name, int recipeCount) {
        return null;
    }
    @Override
    public boolean isEditable() {
        return editable;
    }
    @Override
    public boolean isParentEditable() {
        return PlanMeal.editable;
    }
    @Override
    public boolean isChildEditable() {
        return false;
    }
    @Override
    public String childName() {
        return "UNKNOWN";
    }
}