package fooderie.mealPlanner.models;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import fooderie._main.models.FooderieRepository;

@Entity
public abstract class Plan implements Serializable {
    @PrimaryKey (autoGenerate = true)
    protected Long planId;
    private Long parentId;
    private String name;
    private int recipeCount;

    @Ignore
    private final PropertiesForPlan m_properties;
    @Ignore
    private final PropertiesForPlan m_propertiesOfChild;
    @Ignore
    private LiveData children;
    @Ignore
    private static PlanRoot ROOT;

    public static PlanRoot getROOT() {
        if (ROOT != null)
            return ROOT;
        ROOT = new PlanRoot(PlanRoot.properties, PlanWeek.properties);
        return ROOT;
    }

    public Plan(Long parentId, String name, int recipeCount, PropertiesForPlan properties, PropertiesForPlan propertiesOfChild) {
        this.parentId = parentId;
        this.name = name;
        this.recipeCount = recipeCount;

        this.m_properties = properties;
        this.m_propertiesOfChild = propertiesOfChild;
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
    public void setRecipeCount(int i) {
        this.recipeCount = i;
    }

    public abstract void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o);
    @SuppressWarnings("unchecked")
    void setLiveDataHelper(LiveData ld, LifecycleOwner owner, Observer o) {
        removeLiveData(owner);
        children = ld;
        children.observe(owner, o);
    }

    public void removeLiveData(LifecycleOwner owner) {
        if (children == null)
            return;

        children.removeObservers(owner);
        children = null;
    }

    public boolean isEditable() {
        return m_properties.editable;
    }
    public boolean isChildEditable() {
        return m_propertiesOfChild.editable;
    }

    public boolean isDraggable() {
        return m_properties.draggable;
    }
    public boolean isChildDraggable() {
        return m_propertiesOfChild.draggable;
    }

    public boolean isSchedulable() {
        return m_properties.schedulable;
    }

    public String childName() {
        return m_propertiesOfChild.name;
    }

    public void changeCount(int value) {
        recipeCount += value;
        if (recipeCount < 0)
            recipeCount = 0;
    }

    @Override
    @SuppressWarnings("DefaultLocale")
    public @NonNull String toString() {
        return String.format("[id:%d, parentId:%d, name:%s]", planId, parentId, name);
    }
}
