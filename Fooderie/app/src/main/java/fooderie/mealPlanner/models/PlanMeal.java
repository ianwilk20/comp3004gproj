package fooderie.mealPlanner.models;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import fooderie.models.FooderieRepository;

@Entity(tableName = "table_PlanMeal",
        indices = {@Index("planId"), @Index("parentId")},
        foreignKeys = @ForeignKey(entity = PlanDay.class,
                parentColumns = "planId",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE)
)
public class PlanMeal extends Plan {
    @Ignore
    public static final String planName = "Meal Plan";

    public PlanMeal(Long parentId, String name, int recipeCount) {
        super(parentId, name, recipeCount);
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        children = repo.getRecipePlans(planId);
        children.observe(owner, o);
    }

    @Override
    public Plan makeChild(Long parentId, String name, int recipeCount) {
        return new PlanMeal(parentId, name, recipeCount);
    }
    @Override
    public boolean isEditable() {
        return editable;
    }
    @Override
    public boolean isParentEditable() {
        return PlanDay.editable;
    }
    @Override
    public boolean isChildEditable() {
        return PlanRecipe.editable;
    }
    @Override
    public String childName() {
        return PlanRecipe.planName;
    }
}
