package fooderie._main.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.example.fooderie.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import fooderie.groceryList.views.GroceryListView;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlanner.views.mpPlanRecipeDisplayView;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.models.ScheduleAndPlanWeek;
import fooderie.scheduler.views.sWeeklyScheduleFragment_Proxy;

public class NotificationHelper {
    private AppCompatActivity m_activity;
    private FooderieRepository m_repo;

    public NotificationHelper(AppCompatActivity activity) {
        m_activity = activity;
        m_repo = new FooderieRepository(activity.getApplication());
    }

    // -- [START] MEAL PLANNER NOTIFICATIONS -- //
    private final String MealPlannerChannelID = "Meal Planner";
    @SuppressWarnings("all")
    public void InitMealPlannerNotification() {
        String description = "Notifications that pertain to the set up of a user's meal plan";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(MealPlannerChannelID, MealPlannerChannelID, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = m_activity.getSystemService(NotificationManager.class);
        notificationManager.deleteNotificationChannel(MealPlannerChannelID);
        notificationManager.createNotificationChannel(channel);

        MealPlannerNotificationOne();
    }

    private void MealPlannerNotificationOne() {
        // -- Notify the user if no weekly meal plans exist -- //
        LiveData<List<PlanWeek>> liveData = m_repo.getWeekPlans();
        Observer<List<PlanWeek>> o = new Observer<List<PlanWeek>>() {
            @Override
            public void onChanged(@Nullable List<PlanWeek> plans) {
                if (plans == null || plans.size() == 0) {
                    Intent intent = new Intent(m_activity.getApplicationContext(), mpPlanRecipeDisplayView.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
                    PendingIntent pendingIntent = PendingIntent.getActivity(m_activity, 0, intent, 0);

                    String title = "No Plans";
                    String text = "Let's create a plan!";

                    MakeMealPlannerNotification(pendingIntent, title, text, 1);
                } else {
                    MealPlannerNotificationTwo();
                }
                liveData.removeObserver(this);
            }
        };
        liveData.observe(m_activity, o);
    }

    private void MealPlannerNotificationTwo() {
        // -- Notify the user if a plan has not been scheduled for next week -- //
        LiveData<List<ScheduleAndPlanWeek>> liveData = m_repo.getAllSchedules();
        Observer<List<ScheduleAndPlanWeek>> o = new Observer<List<ScheduleAndPlanWeek>>() {
            @Override
            public void onChanged(@Nullable List<ScheduleAndPlanWeek> schedules) {
                ScheduleAndPlanWeek s = findMatchingWeek(schedules);

                if (s == null || s.pw == null || s.s.getPlanWeekId() != null) {
                    MealPlannerNotificationThree();
                } else {
                    Intent intent = new Intent(m_activity, sWeeklyScheduleFragment_Proxy.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);;
                    PendingIntent pendingIntent = PendingIntent.getActivity(m_activity, 0, intent, 0);

                    String title = "Next Week's Plan";
                    String text = "Look's like there is no plan scheduled for next week. Let's schedule it!";

                    MakeMealPlannerNotification(pendingIntent, title, text, 2);
                }

                liveData.removeObserver(this);
            }
        };
        liveData.observe(m_activity, o);
    }

    private ScheduleAndPlanWeek findMatchingWeek(List<ScheduleAndPlanWeek> schedules) {
        if (schedules == null)
            return null;
        Long next = Schedule.getNextWeekID();
        for (ScheduleAndPlanWeek s : schedules)
            if (s.s.getWeekOfYearId().equals(next))
                return s;
        return null;
    }

    private void MealPlannerNotificationThree() {
        // -- Notify the user if they would like to go shopping for next weeks ingredients -- //
        Intent intent = new Intent(m_activity, GroceryListView.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity( m_activity, 0, intent, 0);

        String title = "Shopping Time";
        String text = "Go get those groceries!";

        MakeMealPlannerNotification(pendingIntent, title, text, 3);
    }

    private void MakeMealPlannerNotification(PendingIntent pendingIntent, String title, String description, int id) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(m_activity.getBaseContext(), MealPlannerChannelID)
                .setSmallIcon(R.drawable.edit_button)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(m_activity);
        notificationManagerCompat.notify(id, notificationBuilder.build());
    }
    // -- [END] MEAL PLANNER NOTIFICATIONS -- //
}

