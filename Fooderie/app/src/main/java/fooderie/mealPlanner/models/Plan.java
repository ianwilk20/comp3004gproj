package fooderie.mealPlanner.models;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fooderie.models.FooderieRepository;

@Entity
public abstract class Plan {
    @PrimaryKey (autoGenerate = true)
    protected Long planId;
    protected Long parentId;
    protected int recipeCount;
    protected String name;

    @Ignore
    public static final boolean editable = true;
    @Ignore
    public static final boolean draggable = false;
    @Ignore
    public static final String planName = "UNKNOWN";
    @Ignore
    protected LiveData children;

    public Plan(Long parentId, String name, int recipeCount) {
        this.parentId = parentId;
        this.name = name;
        this.recipeCount = recipeCount;
    }

    @Ignore
    public Plan(Long planId, Long parentId, String name, int recipeCount) {
        this.planId = planId;
        this.parentId = parentId;
        this.name = name;
        this.recipeCount = recipeCount;
    }

    public String getName() {return name;}
    public Long getPlanId() {
        return planId;
    }
    public void setPlanId(Long planId) {
        this.planId = planId;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long id) {
        this.parentId = id;
    }
    public int getRecipeCount() {return recipeCount;}
    public void setRecipeCount(int r) {this.recipeCount = r;}

    public abstract void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o);

    public abstract boolean isEditable();
    public abstract boolean isChildEditable();

    public abstract boolean isDraggable();
    public abstract boolean isChildDraggable();

    public abstract String childName();

    public void removeLiveData(@NonNull LifecycleOwner owner) {
        if (children == null)
            return;

        children.removeObservers(owner);
        children = null;
    }

    @Override
    @SuppressWarnings("All")
    public @NonNull String toString() {
        return String.format("[id:%d, parentId:%d, name:%s]", planId, parentId, name);
    }
}
