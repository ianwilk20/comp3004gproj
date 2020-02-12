package fooderie.mealPlanner.models;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Ignore;
import fooderie.models.FooderieRepository;

public class PlanRoot extends Plan {
    public static final boolean editable = false;
    public PlanRoot() {
        super(null, null, null,0);
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        children = repo.getWeekPlans();
        children.observe(owner, o);
    }

    @Override
    public Plan makeChild(Long parentId, String name, int recipeCount) {
        return new PlanWeek(parentId, name, recipeCount);
    }
    @Override
    public boolean isEditable() {
        return editable;
    }
    @Override
    public boolean isParentEditable() {
        return false;
    }
    @Override
    public boolean isChildEditable() {
        return PlanWeek.editable;
    }
    @Override
    public String childName() {
        return PlanWeek.planName;
    }
}
