package fooderie.mealPlannerScheduler.viewModels;

import android.app.Application;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.mealPlannerScheduler.models.ScheduleAndPlanWeek;
import fooderie.models.FooderieRepository;

public class WeeklyScheduleViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;

    public WeeklyScheduleViewModel(Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
    }

    public LiveData<List<ScheduleAndPlanWeek>> getSchedules() {
        return m_repo.getAllSchedules();
    }

    public void updateSchedule(Schedule s) {
        m_repo.update(s);
    }
}
