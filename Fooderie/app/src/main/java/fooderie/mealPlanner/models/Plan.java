package fooderie.mealPlanner.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_Plan",
        indices = {@Index("plan_id"), @Index("parent_id")},
        foreignKeys = @ForeignKey(entity = Plan.class,
            parentColumns = "plan_id",
            childColumns = "parent_id",
            onDelete = ForeignKey.CASCADE)
        )
public class Plan {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="plan_id")
    private Long id;
    @ColumnInfo(name="parent_id")
    private Long parentId;
    private int depth;
    private int recipeCount;
    private String name;

    public Plan(Long parentId, String name, int depth, int recipeCount) {
        this.parentId = parentId;
        this.name = name;
        this.depth = depth;
        this.recipeCount = recipeCount;
    }

    @Ignore
    public Plan(Long id, Long parentId, String name, int depth, int recipeCount) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.depth = depth;
        this.recipeCount = recipeCount;
    }
    @Ignore
    public Plan(Plan p) {
        this.id = p.id;
        this.parentId = p.parentId;
        this.name = p.name;
        this.depth = p.depth;
        this.recipeCount = p.recipeCount;
    }

    public Long getParentId() {return parentId;}
    public String getName() {return name;}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getDepth() {return depth;}
    public void setDepth(int d) {this.depth = d;}
    public int getRecipeCount() {return recipeCount;}
    public void setRecipeCount(int r) {this.recipeCount = r;}

    @Override
    public String toString() {
        return String.format("[id:%d, parentId:%d, name:%s]", id, parentId, name);
    }
}
