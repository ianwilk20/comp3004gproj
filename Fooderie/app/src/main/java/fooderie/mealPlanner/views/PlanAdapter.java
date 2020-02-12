package fooderie.mealPlanner.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooderie.R;

import java.util.List;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder>{
    class PlanViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView recipeCount;
        private final ImageView deleteButton;
        private final ConstraintLayout layout;
        private PlanViewHolder(View item) {
            super(item);
            title = item.findViewById(R.id.planItemTitle);
            recipeCount = item.findViewById(R.id.planItemRecipeCount);
            deleteButton = item.findViewById(R.id.planItemDeleteButton);
            layout = item.findViewById(R.id.planItem);
        }
    }

    private final LayoutInflater m_inflater;
    private List<Plan> m_displayPlans;
    private Function<Plan, Void> m_displayChildrenPlansOfID;
    private Function<Plan, Void> m_deletePlan;

    PlanAdapter(Context context, Function<Plan, Void> display, Function<Plan, Void> delete) {
        m_inflater = LayoutInflater.from(context);
        m_displayChildrenPlansOfID = display;
        m_deletePlan = delete;
    }

    @Override
    public @NonNull PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = m_inflater.inflate(R.layout.plan_recyclerview_item, parent, false);
        return new PlanViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int pos) {
        if (m_displayPlans != null) {
            Plan p = m_displayPlans.get(pos);
            holder.title.setText(p.getName().toUpperCase());
            holder.recipeCount.setText(String.format("%d: Recipe(s)", (p.getRecipeCount())));

            holder.layout.setOnClickListener((View v)-> {
                m_displayChildrenPlansOfID.apply(p);
            });

            if (p.isEditable()) {
                holder.deleteButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setOnClickListener((View v) -> {
                    m_deletePlan.apply(p);
                });
            } else {
                holder.deleteButton.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.title.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return (m_displayPlans != null) ? m_displayPlans.size() : 0;
    }

    void setPlans(List<Plan> plans) {
        this.m_displayPlans = plans;
        notifyDataSetChanged();
    }
}
