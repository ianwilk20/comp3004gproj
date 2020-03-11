package fooderie.mealPlanner.models;

public class PropertiesForPlan {
    boolean editable;
    boolean draggable;
    boolean schedulable;
    public String name;

    PropertiesForPlan() {
        this(false, false, false, "UNKNOWN");
    }
    PropertiesForPlan(boolean editable, boolean draggable, boolean schedulable, String name) {
        this.editable = editable;
        this.draggable = draggable;
        this.schedulable = schedulable;
        this.name = name;
    }
}
