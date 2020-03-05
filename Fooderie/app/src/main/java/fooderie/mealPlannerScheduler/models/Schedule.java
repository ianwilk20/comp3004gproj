package fooderie.mealPlannerScheduler.models;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fooderie.mealPlanner.models.PlanWeek;

@Entity(tableName = "table_Schedule",
        indices = {@Index("weekOfYearId"), @Index("planWeekId")},
        foreignKeys = @ForeignKey(entity = PlanWeek.class,
            parentColumns = "planId",
            childColumns = "planWeekId",
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL)
        )
public class Schedule  {
    @PrimaryKey
    @NonNull
    private Long weekOfYearId;
    private Long planWeekId;

    public @NonNull Long getWeekOfYearId() {
        return weekOfYearId;
    }
    public Long getPlanWeekId() {
        return planWeekId;
    }
    public void setWeekOfYearId(@NonNull Long id) {
        weekOfYearId = id;
    }
    public void setPlanWeekId(Long id) {
        planWeekId = id;
    }


    public Schedule(@NonNull Long weekOfYearId, Long planWeekId) {
        this.weekOfYearId = weekOfYearId;
        this.planWeekId = planWeekId;
    }
}
