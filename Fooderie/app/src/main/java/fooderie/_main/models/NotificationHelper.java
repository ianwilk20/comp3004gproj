package fooderie._main.models;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.fooderie.R;

import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import fooderie._main.MainActivity;
import fooderie.groceryList.views.GroceryListView;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlanner.views.mpPlanRecipeDisplayView;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.models.ScheduleAndPlanWeek;
import fooderie.scheduler.views.sWeeklyScheduleFragment_Proxy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static fooderie._main.App.CHANNEL_1_ID;

public class NotificationHelper {
    private AppCompatActivity m_activity;
    private FooderieRepository m_repo;

    private NotificationManagerCompat notificationManager;


    public NotificationHelper(AppCompatActivity activity) {
        m_activity = activity;
        m_repo = new FooderieRepository(activity.getApplication());

        notificationManager = NotificationManagerCompat.from(m_activity);
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

    public void ScheduleNextWeekNotifications(List<PlanMeal> mealPlans, AppCompatActivity intentLocation)
    {
        System.out.println(mealPlans);

        Toast.makeText(m_activity, "Reminders set", Toast.LENGTH_SHORT).show();

        Calendar c = GregorianCalendar.getInstance();   //Create a new calendar class
        c.set(Calendar.HOUR_OF_DAY, 0); //Clear the calendar
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.HOUR);
        c.clear(Calendar.SECOND);

        //c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); //Start of this week
        //long timeInMil = c.getTimeInMillis();

        //Get the Calendar day for the start of the next week (starting Monday)
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.MONDAY)
            c.add(Calendar.WEEK_OF_YEAR, 1);    //Add a week since we are at the start of the current week
        else
        {
            while(true)
            {
                day = c.get(Calendar.DAY_OF_WEEK);
                if (day == Calendar.MONDAY)
                    break;

                c.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        long timeInMil2 = c.getTimeInMillis();



        //Intent intent = new Intent(m_activity, ReminderBroadcast.class);
        Intent intent = new Intent(m_activity.getBaseContext(), MainActivity.class);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(m_activity.getBaseContext(), requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(m_activity.getBaseContext(), requestID + 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) m_activity.getSystemService(ALARM_SERVICE);

        long timeAtClick = System.currentTimeMillis();
        long addTime = 1000 * 10;


        for (PlanMeal meal : mealPlans)
        {
            meal.getHour();
            meal.getMinute();
            //meal.getDay();
        }

        //Add our new alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtClick + addTime, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtClick + addTime + addTime, pendingIntent2);

        //alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

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

    private void MakeMealPlannerNotification2(PendingIntent pendingIntent, String title, String description, String ChannelId, int id)
    {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(m_activity.getBaseContext(), ChannelId)
                .setSmallIcon(R.drawable.fooderie_icon)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(m_activity);
        notificationManagerCompat.notify(id, notificationBuilder.build());
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

