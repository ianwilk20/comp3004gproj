package fooderie.mealPlannerScheduler.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fooderie.mealPlanner.models.PlanWeek;

@Entity(tableName = "table_Schedule",
        indices = {@Index("weekOfYearId"), @Index("planId")},
        foreignKeys = @ForeignKey(entity = PlanWeek.class,
            parentColumns = "planId",
            childColumns = "planId",
            onDelete = ForeignKey.CASCADE)
        )
public class Schedule {
    @PrimaryKey
    @NonNull
    private Long weekOfYearId;
    private Long planId;
    private String name;

    public @NonNull Long getWeekOfYearId() {
        return weekOfYearId;
    }
    public Long getPlanId() {
        return planId;
    }
    public String getName() {return name;}
    public void setWeekOfYearId(@NonNull Long id) {
        weekOfYearId = id;
    }
    public void setPlanId(Long id) {
        planId = id;
    }
    public void setName(String s) {
        name = s;
    }


    public Schedule(@NonNull Long weekOfYearId, Long planId, String name) {
        this.weekOfYearId = weekOfYearId;
        this.planId = planId;
        this.name = name;
    }
}
