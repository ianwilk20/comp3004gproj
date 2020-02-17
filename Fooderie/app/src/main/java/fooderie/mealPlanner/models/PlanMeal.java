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
public class PlanMeal extends Plan implements Comparable<PlanMeal>{
    private int order;
    @Ignore
    public static final String planName = "Meal Plan";
    @Ignore
    public static final boolean draggable = true;

    public PlanMeal(Long parentId, String name, int recipeCount, int order) {
        super(parentId, name, recipeCount);
        this.order = order;
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        children = repo.getRecipePlans(planId);
        children.observe(owner, o);
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public int getOrder() {
        return order;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }
    @Override
    public boolean isChildEditable() {
        return false;
    }
    @Override
    public boolean isDraggable() {
        return draggable;
    }
    @Override
    public boolean isChildDraggable() {
        return false;
    }
    @Override
    public String childName() {
        return "UNKNOWN";
    }

    @Override
    public int compareTo(PlanMeal a) {
        return order - a.order;
    }
}
