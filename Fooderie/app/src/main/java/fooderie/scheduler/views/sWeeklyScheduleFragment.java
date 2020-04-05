package fooderie.scheduler.views;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooderie.R;

import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.List;

import fooderie._main.MainActivity;
import fooderie._main.models.ReminderBroadcast;
import fooderie.mealPlanner.views.mpPlanRecipeDisplayView;
import fooderie.scheduler.models.Schedule;
import fooderie.scheduler.models.ScheduleAndPlanWeek;
import fooderie.scheduler.models.ScheduleNotification;
import fooderie.scheduler.viewModels.ScheduleHelperViewModel;
import fooderie.scheduler.viewModels.WeeklyScheduleViewModel;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class sWeeklyScheduleFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private WeeklyScheduleViewModel m_viewModel;

    private static final int PLANRECIPE_REQUEST_VIEW = 1;
    private Schedule m_scheduleToModify;

    public sWeeklyScheduleFragment() {
    }

    @SuppressWarnings("unused")
    public static sWeeklyScheduleFragment newInstance(int columnCount) {
        sWeeklyScheduleFragment fragment = new sWeeklyScheduleFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_viewModel = new ViewModelProvider(this).get(WeeklyScheduleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weeklyschedule, container, false);

        // -- Set the recycler view to display all of the Schedules for the next 52 weeks --//
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.WeeklyScheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        AdapterWeeklySchedule adaptor = new AdapterWeeklySchedule(mListener, this::setWeeklySchedule);
        TextView noPlans = view.findViewById(R.id.WeeklyScheduleNoMealPlans);
        m_viewModel.getSchedules().observe(getViewLifecycleOwner(), (List<ScheduleAndPlanWeek> objects) -> {
            noPlans.setVisibility((objects.size() == 0) ? View.VISIBLE : View.INVISIBLE);
            adaptor.setDisplaySchedules(objects);
        });
        recyclerView.setAdapter(adaptor);

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLANRECIPE_REQUEST_VIEW)
        {
            if (resultCode != RESULT_OK)
                return;

            // -- Get PlanWeek to modify selected DB entry -- //
            long l = data.getLongExtra(mpPlanRecipeDisplayView.PLANWEEK_KEY, -1);
            if (l == -1 || m_scheduleToModify == null)
                return;

            // -- Update the schedule and set to null -- //
            m_scheduleToModify.setPlanWeekId(l);
            m_viewModel.updateSchedule(m_scheduleToModify);
            m_scheduleToModify = null;

            // -- SCHEDULE NOTIFICATIONS FOR COOKING ASSISTANT -- //

            //Create and clear the calendar
            Calendar startDay = GregorianCalendar.getInstance();
            startDay.set(Calendar.DAY_OF_WEEK, 1);
            startDay.set(Calendar.HOUR_OF_DAY, 0);
            startDay.clear(Calendar.MINUTE);
            startDay.clear(Calendar.HOUR);
            startDay.clear(Calendar.SECOND);
            startDay.add(Calendar.WEEK_OF_YEAR, 1);   //Start of next week
            Calendar endDay = (GregorianCalendar)(startDay.clone());
            endDay.add(Calendar.WEEK_OF_YEAR, 1);   //End of next week

            ScheduleHelperViewModel sh = new ViewModelProvider(this).get(ScheduleHelperViewModel.class);
            sh.getScheduleNotifications(getViewLifecycleOwner(), (List<ScheduleNotification> list) -> {
                if (list.size() == 0)
                {
                    Log.d("sWeeklyScheduleFragment", "NO DATA");
                    return;
                }

                for (ScheduleNotification sn : list)
                {
                    Log.d("sWeeklyScheduleFragment", sn.toString());

                    //Create a new calendar and reset its date
                    Calendar c = new GregorianCalendar();
                    c.setTime(sn.getDate());

                    long timeInMillis = c.getTimeInMillis();
                    if (startDay.getTimeInMillis() <= timeInMillis && timeInMillis < endDay.getTimeInMillis())    //If within the next week, use the time
                        ScheduleNextWeekNotifications(sn, timeInMillis);

                }
            });

            Toast.makeText(getActivity(), "Reminders set", Toast.LENGTH_SHORT).show();
        }
    }

    private void ScheduleNextWeekNotifications(ScheduleNotification sn, Long timeInMillis)
    {
        String title = "Fooderie Meal Plan Reminder";
        String description = "Reminder to start cooking your meal plan!";

        //Create our notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "channel1")
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_stat_access_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.fooderie_icon_p))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        //Create our pending intent for where we want the notification to return
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(getContext(), timeInMillis.intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendIntent);
        Notification notification = builder.build();

        //Created the notification to be used later
        Intent notificationIntent = new Intent(getContext(), ReminderBroadcast.class);
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_ID, timeInMillis);
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), timeInMillis.intValue(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Set our notification in the future using the alarm manager, using the time for the alarm to override and not create multiple notifications for the same time
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        Toast.makeText(getActivity(), "Reminders set for " + sn.toString(), Toast.LENGTH_SHORT).show();
    }


    private Void setWeeklySchedule(Schedule s) {
        Intent intent = new Intent(getActivity(), mpPlanRecipeDisplayView.class);

        intent.putExtra(mpPlanRecipeDisplayView.LOOKING_FOR_PLANWEEK_KEY, true);
        m_scheduleToModify = s;

        startActivityForResult(intent, PLANRECIPE_REQUEST_VIEW);
        return null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Schedule item);
    }
}
