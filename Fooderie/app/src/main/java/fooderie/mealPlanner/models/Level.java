package fooderie.mealPlanner.models;

public class Level {
    private Long id;
    private String name;
    private boolean editable;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isEditable() {
        return editable;
    }

    public Level(Long id, String name, boolean editable) {
        this.id = id;
        this.name = name;
        this.editable = editable;
    }
}
