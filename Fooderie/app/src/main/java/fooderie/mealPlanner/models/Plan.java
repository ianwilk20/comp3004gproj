package fooderie.mealPlanner.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
    private String name;

    public Plan(Long parentId, String name) {
        this.parentId = parentId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {return parentId;}
    public String getName() {return name;}

    @Override
    public String toString() {
        return String.format("[id:%d, parentId:%d, name:%s]", id, parentId, name);
    }
}
