package fooderie.mealPlannerScheduler.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooderie.R;

import fooderie.mealPlanner.models.PlanWeek;
import fooderie.mealPlanner.views.PlanRecipeRecyclerView;
import fooderie.mealPlannerScheduler.models.Schedule;
import fooderie.mealPlannerScheduler.viewModels.WeeklyScheduleViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WeeklyScheduleFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private WeeklyScheduleViewModel m_viewModel;

    private static final int PLANRECIPE_REQUEST_VIEW = 1;
    private Schedule m_scheduleToModify;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeeklyScheduleFragment() {
    }

    @SuppressWarnings("unused")
    public static WeeklyScheduleFragment newInstance(int columnCount) {
        WeeklyScheduleFragment fragment = new WeeklyScheduleFragment();

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
        m_viewModel.getSchedules().observe(getViewLifecycleOwner(), adaptor::setDisplaySchedules);
        recyclerView.setAdapter(adaptor);

        return view;
    }


    @Override
    public void onAttach(Context context) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLANRECIPE_REQUEST_VIEW) {
            if (resultCode != RESULT_OK)
                return;

            // -- Get PlanWeek to modify selected DB entry -- //
            PlanWeek p = (PlanWeek) data.getSerializableExtra(PlanRecipeRecyclerView.PLANWEEK_KEY);
            if (p == null || m_scheduleToModify == null)
                return;

            m_scheduleToModify.setPlanId(p.getPlanId());
            m_scheduleToModify.setName(p.getName());
            m_viewModel.updateSchedule(m_scheduleToModify);
            m_scheduleToModify = null;
        }
    }

    public Void setWeeklySchedule(Schedule s) {
        Intent intent = new Intent(getActivity(), PlanRecipeRecyclerView.class);

        intent.putExtra(PlanRecipeRecyclerView.LOOKING_FOR_PLANWEEK_KEY, true);
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
