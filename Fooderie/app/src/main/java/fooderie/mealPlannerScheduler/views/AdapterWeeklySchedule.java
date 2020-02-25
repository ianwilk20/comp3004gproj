package fooderie.mealPlannerScheduler.views;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooderie.R;

import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.mealPlannerScheduler.views.WeeklyScheduleFragment.OnListFragmentInteractionListener;

import java.util.Calendar;
import java.util.List;

public class AdapterWeeklySchedule extends RecyclerView.Adapter<AdapterWeeklySchedule.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mWeekNum;
        private final TextView mPlanName;
        public ViewHolder(View view) {
            super(view);
            mWeekNum = view.findViewById(R.id.WeeklyScheduleItemWeekNumber);
            mPlanName = view.findViewById(R.id.WeeklyScheduleItemPlanName);
        }
    }

    private List<Schedule> m_schedules;
    private final OnListFragmentInteractionListener mListener;

    void setDisplaySchedules(List<Schedule> s) {
        //TODO: ENSURE PROPER SORTED ORDER
        //Calendar calender = Calendar.getInstance();
        //Long weekNum = (Long) Integer.toUnsignedLong(calender.get(Calendar.WEEK_OF_YEAR));
        m_schedules = s;
        notifyDataSetChanged();
    }

    public AdapterWeeklySchedule(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weeklyschedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Schedule s = m_schedules.get(position);
        holder.mPlanName.setText(s.getName());
        holder.mWeekNum.setText(s.getWeekOfYearId().toString());
    }

    @Override
    public int getItemCount() {
        return (m_schedules == null) ? 0 : m_schedules.size();
    }
}
