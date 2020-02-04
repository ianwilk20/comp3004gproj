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
    private int id;
    @ColumnInfo(name="parent_id")
    private Integer parentId;
    private int level;
    private String name;

    public Plan(Integer parentId, int level, String name) {
        this.parentId = parentId;
        this.level = level;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getParentId() {return parentId;}
    public int getLevel() {return level;}
    public String getName() {return name;}
}
