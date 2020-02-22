package fooderie.mealPlanner.models;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import fooderie.models.FooderieRepository;

@Entity(tableName = "table_PlanDay",
        indices = {@Index("planId"), @Index("parentId")},
        foreignKeys = @ForeignKey(entity = PlanWeek.class,
                parentColumns = "planId",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE)
)
public class PlanDay extends Plan {
    @Ignore
    public static final String planName = "Day Plan";
    @Ignore
    public static final boolean editable = false;

    public PlanDay(Long parentId, String name, int recipeCount) {
        super(parentId, name, recipeCount);
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        children = repo.getMealPlans(planId);
        children.observe(owner, o);
    }

    @Override
    public void removeLiveData(LifecycleOwner owner){
        if (children == null)
            return;

        children.removeObservers(owner);
        children = null;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }
    @Override
    public boolean isChildEditable() {
        return PlanMeal.editable;
    }
    @Override
    public boolean isDraggable() {
        return draggable;
    }
    @Override
    public boolean isChildDraggable() {
        return PlanMeal.draggable;
    }
    @Override
    public String childName() {
        return PlanMeal.planName;
    }
}
