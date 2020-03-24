package fooderie.scheduler.viewModels;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.models.ScheduleAndPlanWeek;
import fooderie._main.models.FooderieRepository;

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
