package fooderie.mealPlanner.views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooderie.R;
import com.kingfisher.easyviewindicator.RecyclerViewIndicator;

import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.SnapHelper;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.viewModels.TodayMealViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TodayMealFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private TodayMealViewModel m_viewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TodayMealFragment() {
    }

    @SuppressWarnings("unused")
    public static TodayMealFragment newInstance(int columnCount) {
        TodayMealFragment fragment = new TodayMealFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_viewModel = new ViewModelProvider(this).get(TodayMealViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todaymeal, container, false);

        // -- Set the widget title to that of the current day of the week -- //
        Calendar calender = Calendar.getInstance();
        TextView tv = view.findViewById(R.id.TodayMealFragmentTitle);
        tv.setText(m_viewModel.dayOfWeekName());

        // -- Set the recycler view to display all of the Meals for the given day --//
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.TodayMealRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        AdapterTodayMeal adaptor = new AdapterTodayMeal(mListener, context);
        recyclerView.setAdapter(adaptor);

        // -- Set the snap helper to snap the items in the RecyclerView -- //
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // -- Set the adapter to have all of the meals to display, ensure indicator is updated, & hide/display empty array indicator text-- //
        TextView nothingPlanned = view.findViewById(R.id.TodayMealFragmentEmpty);
        RecyclerViewIndicator horizontalIndicator = view.findViewById(R.id.recyclerViewIndicator);
        horizontalIndicator.setRecyclerView(recyclerView);
        m_viewModel.getMealsForToday().observe(getViewLifecycleOwner(), (List<PlanMeal> meals) -> {
            if (meals.size() == 0) {
                nothingPlanned.setVisibility(View.VISIBLE);
            } else {
                nothingPlanned.setVisibility(View.INVISIBLE);
            }

            adaptor.setDisplayMeals(meals);
            horizontalIndicator.forceUpdateItemCount();
        });

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
        void onListFragmentInteraction(PlanMeal meal);
    }
}
