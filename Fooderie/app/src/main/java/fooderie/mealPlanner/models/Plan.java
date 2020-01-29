package fooderie.mealPlanner.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_Plan",
        foreignKeys = @ForeignKey(entity = Plan.class,
            parentColumns = "id",
            childColumns = "parentId",
            onDelete = ForeignKey.CASCADE)
        )
public class Plan {
    @PrimaryKey
    private int id;
    private int parentId;
    private int level;
    private String name;

    public int getId() {return id;}
    public int getParentId() {return parentId;}
    public int getLevel() {return level;}
    public String getName() {return name;}
}
