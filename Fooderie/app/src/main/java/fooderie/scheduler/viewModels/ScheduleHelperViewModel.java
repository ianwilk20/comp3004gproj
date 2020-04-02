package fooderie.scheduler.viewModels;

import android.app.Application;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import fooderie._main.models.FooderieRepository;
import fooderie.scheduler.models.ScheduleNotification;
import fooderie.scheduler.models.ScheduleNotificationFromDao;

public class ScheduleHelperViewModel extends AndroidViewModel {
    public interface ScheduleNotificationList {
        void execute(List<ScheduleNotification> scheduleNotifications);
    }

    private FooderieRepository m_repo;
    public ScheduleHelperViewModel(Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
    }

    public void getScheduleNotifications(LifecycleOwner owner, ScheduleNotificationList callback) {
        LiveData<List<ScheduleNotificationFromDao>> data = m_repo.getScheduleNotificationFromDao();
        getScheduleNotifications(data, owner, callback);
    }

    public void getScheduleNotifications(Long weekId, LifecycleOwner owner, ScheduleNotificationList callback) {
        LiveData<List<ScheduleNotificationFromDao>> data = m_repo.getScheduleNotificationFromDao(weekId);
        getScheduleNotifications(data, owner, callback);
    }

    private void getScheduleNotifications(LiveData<List<ScheduleNotificationFromDao>> data, LifecycleOwner owner, ScheduleNotificationList callback) {
        Observer<List<ScheduleNotificationFromDao>> o = new Observer<List<ScheduleNotificationFromDao>>() {
            @Override
            public void onChanged(@Nullable List<ScheduleNotificationFromDao> list) {
                List<ScheduleNotification> notifications = new ArrayList<>();

                Calendar c = Calendar.getInstance();
                int current = c.get(Calendar.WEEK_OF_YEAR);

                if (list != null && list.size() > 0) {
                    for (ScheduleNotificationFromDao x : list) {
                        String t = x.meal.getName();
                        int r = x.meal.getRecipeCount();

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());

                        int week = x.schedule.getWeekOfYearId().intValue();
                        int year = cal.get(Calendar.YEAR);
                        year += (week < cal.get(Calendar.WEEK_OF_YEAR)) ? 1 : 0;

                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.WEEK_OF_YEAR, week);
                        cal.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(x.day.getName()).getValue() + 1 );
                        cal.set(Calendar.HOUR_OF_DAY, x.meal.getHour());
                        cal.set(Calendar.MINUTE, x.meal.getMinute());
                        cal.set(Calendar.SECOND, 0);
                        Date d = cal.getTime();

                        ScheduleNotification sn = new ScheduleNotification(d, t, r);
                        notifications.add(sn);
                    }
                }

                callback.execute(notifications);
                data.removeObserver(this);
            }
        };
        data.observe(owner, o);
    }

    public static Long getNextWeekId() {
        Calendar c = Calendar.getInstance();
        return Integer.toUnsignedLong((c.get(Calendar.WEEK_OF_YEAR )+ 1) % 53);
    }
}
