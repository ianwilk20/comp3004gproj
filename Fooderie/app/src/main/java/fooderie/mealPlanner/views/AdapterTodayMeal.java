package fooderie.mealPlanner.views;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooderie.R;

import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.views.TodayMealRecyclerViewFragment.OnListFragmentInteractionListener;

import java.util.List;

public class AdapterTodayMeal extends RecyclerView.Adapter<AdapterTodayMeal.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final RecyclerView mRecyclerView;

        private ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.TodayMealTitle);
            mRecyclerView = view.findViewById(R.id.TodayMealRecyclerView);
        }
    }

    private List<PlanMeal> m_meal;
    private final Context m_context;
    private final OnListFragmentInteractionListener mListener;

    void setDisplayMeals(List<PlanMeal> meals) {
        m_meal = meals;
        notifyDataSetChanged();
    }

    public AdapterTodayMeal(OnListFragmentInteractionListener listener, Context context) {
        mListener = listener;
        m_context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_todaymeal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PlanMeal p = m_meal.get(position);

        holder.mTitle.setText(p.getName());

        /*AdapterTodayRecipe adaptor = new AdapterTodayRecipe(t);
        holder.mRecyclerView.setAdapter(adaptor);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(m_context));*/
    }

    @Override
    public int getItemCount() {
        return (m_meal == null) ? 0 : m_meal.size();
    }
}
