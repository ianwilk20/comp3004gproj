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
    public Plan makeChild(Long parentId, String name, int recipeCount) {
        return new PlanDay(parentId, name, recipeCount);
    }
    @Override
    public boolean isEditable() {
        return editable;
    }
    @Override
    public boolean isParentEditable() {
        return PlanRoot.editable;
    }
    @Override
    public boolean isChildEditable() {
        return PlanDay.editable;
    }
    @Override
    public String childName() {
        return PlanDay.planName;
    }
}
