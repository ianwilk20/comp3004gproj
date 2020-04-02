package fooderie.scheduler.models;

import androidx.room.Embedded;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;

public class ScheduleNotificationFromDao {
    @Embedded(prefix = "pd_")
    public PlanDay day;
    @Embedded(prefix = "pm_")
    public PlanMeal meal;
    @Embedded(prefix = "s_")
    public Schedule schedule;
}
