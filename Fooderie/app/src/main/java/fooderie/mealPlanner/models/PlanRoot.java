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
        return PlanWeek.editable;
    }
    @Override
    public boolean isDraggable() {
        return draggable;
    }
    @Override
    public boolean isChildDraggable() {
        return PlanWeek.draggable;
    }
    @Override
    public String childName() {
        return PlanWeek.planName;
    }
}
