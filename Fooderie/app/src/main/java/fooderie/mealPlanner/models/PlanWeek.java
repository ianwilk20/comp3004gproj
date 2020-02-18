package fooderie.mealPlanner.models;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import fooderie.models.FooderieRepository;

@Entity(tableName = "table_PlanWeek",
        indices = {@Index("planId"), @Index("parentId")}
        )
public class PlanWeek extends Plan{
    @Ignore
    public static final String planName = "Week Plan";

    public PlanWeek(Long parentId, String name, int recipeCount) {
        super(null, name, recipeCount);
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        children = repo.getDayPlans(planId);
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
        return PlanDay.editable;
    }
    @Override
    public boolean isDraggable() {
        return draggable;
    }
    @Override
    public boolean isChildDraggable() {
        return PlanDay.draggable;
    }
    @Override
    public String childName() {
        return PlanDay.planName;
    }
}
