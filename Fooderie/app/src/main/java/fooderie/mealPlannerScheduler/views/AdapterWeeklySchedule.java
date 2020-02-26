package fooderie.mealPlannerScheduler.views;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooderie.R;

import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.mealPlannerScheduler.views.WeeklyScheduleFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class AdapterWeeklySchedule extends RecyclerView.Adapter<AdapterWeeklySchedule.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout mLayout;
        private final TextView mWeekNum;
        private final TextView mPlanName;
        public ViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.WeeklyScheduleItemLayout);
            mWeekNum = view.findViewById(R.id.WeeklyScheduleItemWeekNumber);
            mPlanName = view.findViewById(R.id.WeeklyScheduleItemPlanName);
        }
    }

    private List<Schedule> m_schedules;
    private final OnListFragmentInteractionListener mListener;
    private final Function<Schedule, Void> mselected;

    void setDisplaySchedules(List<Schedule> schedules) {
        if(schedules.size() > 0) {
            Calendar calender = Calendar.getInstance();
            Long weekNum = (Long) Integer.toUnsignedLong(calender.get(Calendar.WEEK_OF_YEAR));

            Collections.sort(schedules);
            int i = 0;
            while (!schedules.get(i).getWeekOfYearId().equals(weekNum)) {
                i++;
                if (i == schedules.size() - 1) break;
            }

            List<Schedule> sendToBack = new ArrayList<>(schedules.subList(0, i));
            List<Schedule> bringToFront = new ArrayList<>(schedules.subList(i, schedules.size()));

            bringToFront.addAll(sendToBack);
            m_schedules = bringToFront;
        } else {
            m_schedules = schedules;
        }
        notifyDataSetChanged();
    }

    public AdapterWeeklySchedule(OnListFragmentInteractionListener listener, Function<Schedule, Void> selected) {
        mListener = listener;
        mselected = selected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weeklyschedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Schedule s = m_schedules.get(position);
        if(s.getName() != null)
            holder.mPlanName.setText(s.getName());
        else
            holder.mPlanName.setText(R.string.nothing_planned);
        holder.mWeekNum.setText(s.getWeekOfYearId().toString());

        holder.mLayout.setOnClickListener((View v) -> {
            mselected.apply(s);
        });
    }

    @Override
    public int getItemCount() {
        return (m_schedules == null) ? 0 : m_schedules.size();
    }
}
