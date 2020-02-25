package fooderie.mealPlanner.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_PlanWeekSchedule",
        indices = {@Index("weekOfYearId"), @Index("planId")},
        foreignKeys = @ForeignKey(entity = PlanWeek.class,
            parentColumns = "planId",
            childColumns = "planId",
            onDelete = ForeignKey.CASCADE)
        )
public class PlanWeekSchedule {
    @PrimaryKey
    @NonNull
    Long weekOfYearId;
    Long planId;

    public Long getWeekOfYearId() {
        return weekOfYearId;
    }
    public Long getPlanId() {
        return planId;
    }
    public void setWeekOfYearId(Long id) {
        weekOfYearId = id;
    }
    public void setPlanId(Long id) {
        planId = id;
    }

    public PlanWeekSchedule(Long weekOfYearId, Long planId) {
        this.weekOfYearId = weekOfYearId;
        this.planId = planId;
    }
}
