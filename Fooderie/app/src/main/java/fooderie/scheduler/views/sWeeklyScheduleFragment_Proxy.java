package fooderie.scheduler.views;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import fooderie.mealPlanner.views.mpPlanRecipeDisplayView;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.models.ScheduleAndPlanWeek;
import fooderie.scheduler.viewModels.WeeklyScheduleViewModel;

public class sWeeklyScheduleFragment_Proxy extends AppCompatActivity {
    private WeeklyScheduleViewModel m_viewModel;
    private Schedule m_scheduleToModify;
    public static final int PLANRECIPE_REQUEST_VIEW = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_viewModel = new ViewModelProvider(this).get(WeeklyScheduleViewModel.class);

        LiveData<List<ScheduleAndPlanWeek>> liveData = m_viewModel.getSchedules();
        Observer<List<ScheduleAndPlanWeek>> o = new Observer<List<ScheduleAndPlanWeek>>() {
            @Override
            public void onChanged(@Nullable List<ScheduleAndPlanWeek> schedules) {
                if (schedules == null)
                    return;
                Long next = Schedule.getNextWeekID();
                for (ScheduleAndPlanWeek s : schedules)
                    if (s.s.getWeekOfYearId().equals(next))
                        setWeeklySchedule(s.s);
                liveData.removeObserver(this);
            }
        };
        liveData.observe(this, o);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLANRECIPE_REQUEST_VIEW) {
            if (resultCode != RESULT_OK)
                return;

            // -- Get PlanWeek to modify selected DB entry -- //
            long l = data.getLongExtra(mpPlanRecipeDisplayView.PLANWEEK_KEY, -1);
            if (l == -1 || m_scheduleToModify == null)
                return;

            m_scheduleToModify.setPlanWeekId(l);
            m_viewModel.updateSchedule(m_scheduleToModify);
            m_scheduleToModify = null;
        }
        finish();
    }

    private void setWeeklySchedule(Schedule s) {
        Intent intent = new Intent(this, mpPlanRecipeDisplayView.class);

        intent.putExtra(mpPlanRecipeDisplayView.LOOKING_FOR_PLANWEEK_KEY, true);
        m_scheduleToModify = s;

        startActivityForResult(intent, PLANRECIPE_REQUEST_VIEW);
    }
}
