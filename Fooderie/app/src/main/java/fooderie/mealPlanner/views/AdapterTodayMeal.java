package fooderie.mealPlanner.views;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooderie.R;

import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.viewModels.TodayMealViewModel;
import fooderie.recipeBrowser.models.Recipe;

import java.security.acl.Owner;
import java.util.List;
import java.util.function.Function;

public class AdapterTodayMeal extends RecyclerView.Adapter<AdapterTodayMeal.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final RecyclerView mRecyclerView;
        private final Context mContext;

        private ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.TodayMealTitle);
            mRecyclerView = view.findViewById(R.id.TodayMealRecipesRecyclerView);
            mContext = view.getContext();
        }
    }

    private List<PlanMeal> m_meals;
    private final TodayMealViewModel m_viewModel;
    private final LifecycleOwner m_owner;
    private final Function<Recipe, Void> mDisplayRecipe;

    void setDisplayMeals(List<PlanMeal> meals) {
        m_meals = meals;
        m_viewModel.clearRecipeLiveData(m_owner);
        notifyDataSetChanged();
    }

    public AdapterTodayMeal(TodayMealViewModel wm, LifecycleOwner o, Function<Recipe, Void> displayRecipe) {
        m_viewModel = wm;
        m_owner = o;
        mDisplayRecipe= displayRecipe;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_todaymeal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PlanMeal p = m_meals.get(position);

        holder.mTitle.setText(p.getName());

        AdapterTodayRecipe adaptor = new AdapterTodayRecipe(mDisplayRecipe);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(holder.mContext));
        holder.mRecyclerView.setAdapter(adaptor);

        m_viewModel.getRecipesFromPlanMeal(p).observe(m_owner, adaptor::setRecipes);
    }

    @Override
    public int getItemCount() {
        return (m_meals == null) ? 0 : m_meals.size();
    }
}
