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
    private Function<Long, Void> m_displayChildrenPlansOfID;

    PlanAdapter(Context context, Function<Long, Void> func) {
        m_inflater = LayoutInflater.from(context);
        m_displayChildrenPlansOfID = func;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = m_inflater.inflate(R.layout.plan_recyclerview_item, parent, false);
        return new PlanViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int pos) {
        if (m_displayPlans != null) {
            Plan p = m_displayPlans.get(pos);
            holder.title.setText(p.getName().toUpperCase());

            holder.layout.setOnClickListener((View v)-> {
                m_displayChildrenPlansOfID.apply(p.getId());
            });
            holder.deleteButton.setOnClickListener((View v) -> {

            });


        } else {
            holder.text.setText("N/A");
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
