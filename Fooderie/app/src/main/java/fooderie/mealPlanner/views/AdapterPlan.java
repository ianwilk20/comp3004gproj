package fooderie.mealPlanner.views;

import android.content.Context;
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

public class AdapterPlan extends RecyclerView.Adapter<AdapterPlan.PlanViewHolder> {
    class PlanViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView recipeCount;
        private final ImageView deleteButton;
        private final ImageView orderButton;
        private final ConstraintLayout layout;

        private PlanViewHolder(View item) {
            super(item);
            title = item.findViewById(R.id.planItemTitle);
            recipeCount = item.findViewById(R.id.planItemRecipeCount);
            deleteButton = item.findViewById(R.id.planItemDeleteButton);
            orderButton = item.findViewById(R.id.planItemOrderButton);
            layout = item.findViewById(R.id.planItem);
        }
    }

    private final LayoutInflater m_inflater;
    private List<Plan> m_displayPlans;
    private Function<Plan, Void> m_displayChildrenPlansOfID;
    private Function<Plan, Void> m_deletePlan;


    AdapterPlan(Context context, Function<Plan, Void> display, Function<Plan, Void> delete) {
        m_inflater = LayoutInflater.from(context);
        m_displayChildrenPlansOfID = display;
        m_deletePlan = delete;
    }

    void setPlans(List<Plan> plans) {
        this.m_displayPlans = plans;
        notifyDataSetChanged();
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

            int count = p.getRecipeCount();
            String suffix = (count == 0 || count > 1) ? "s" : "";
            holder.recipeCount.setText(Integer.toString(count) + " Recipe" + suffix);

            holder.layout.setOnClickListener((View v) -> m_displayChildrenPlansOfID.apply(p));

            if (p.isEditable()) {
                holder.deleteButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setOnClickListener((View v) -> m_deletePlan.apply(p));
            } else {
                holder.deleteButton.setVisibility(View.INVISIBLE);
            }

            if (p.isDraggable()) {
                holder.orderButton.setVisibility(View.VISIBLE);
            } else {
                holder.orderButton.setVisibility(View.INVISIBLE);
            }

        } else {
            holder.title.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return (m_displayPlans != null) ? m_displayPlans.size() : 0;
    }
}
