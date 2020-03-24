package fooderie.scheduler.models;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import fooderie.mealPlanner.models.PlanWeek;

public class ScheduleAndPlanWeek implements Serializable, Comparable<ScheduleAndPlanWeek> {
    @Embedded
    public Schedule s;
    @Embedded
    public PlanWeek pw;

    @Override
    public int compareTo(@NonNull ScheduleAndPlanWeek obj) {
        return (int) (s.getWeekOfYearId() - obj.s.getWeekOfYearId());
    }
}
