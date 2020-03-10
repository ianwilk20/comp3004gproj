package fooderie.mealPlanner.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooderie.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanWeek;

public class AdapterPlan extends RecyclerView.Adapter<AdapterPlan.PlanViewHolder> {
    class PlanViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView recipeCount;
        private final TextView scheduleButton;
        private final ImageView deleteButton;
        private final ImageView orderButton;
        private final ImageView rightArrowButton;
        private final ConstraintLayout layout;
        private final Button selectButton;

        private PlanViewHolder(View item) {
            super(item);
            title = item.findViewById(R.id.planItemTitle);
            recipeCount = item.findViewById(R.id.planItemRecipeCount);
            deleteButton = item.findViewById(R.id.planItemDeleteButton);
            orderButton = item.findViewById(R.id.planItemOrderButton);
            layout = item.findViewById(R.id.planItem);
            selectButton = item.findViewById(R.id.planItemSelectButton);
            rightArrowButton = item.findViewById(R.id.planItemRightArrow);
            scheduleButton = item.findViewById(R.id.planItemSchedule);
        }
    }

    private final LayoutInflater m_inflater;
    private List<Plan> m_displayPlans;
    private Function<Plan, Void> m_displayChildrenPlansOfID;
    private Function<Plan, Void> m_deletePlan;
    private Function<PlanWeek, Void> m_selectPlan;
    private Function<Plan, Void> m_updatePlan;

    AdapterPlan(Context context, Function<Plan, Void> display, Function<Plan, Void> delete, Function<PlanWeek, Void> select, Function<Plan, Void> update) {
        m_inflater = LayoutInflater.from(context);
        m_displayChildrenPlansOfID = display;
        m_deletePlan = delete;
        m_selectPlan = select;
        m_updatePlan = update;
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
        if (m_displayPlans == null) {
            holder.title.setText("N/A");
            return;
        }

        Plan p = m_displayPlans.get(pos);
        holder.title.setText(p.getName().toUpperCase());

        int count = p.getRecipeCount();
        String suffix = (count == 0 || count > 1) ? "s" : "";
        holder.recipeCount.setText(Integer.toString(count) + " Recipe" + suffix);

        // -- If the meal planner is being accessed to select a schedule hide everything -- //
        if (p instanceof PlanWeek && m_selectPlan != null) {
            holder.selectButton.setOnClickListener((View v) -> m_selectPlan.apply((PlanWeek) p));
            holder.selectButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.GONE);
            holder.rightArrowButton.setVisibility(View.GONE);
            holder.orderButton.setVisibility(View.GONE);
            holder.scheduleButton.setVisibility(View.GONE);
            return;
        }

        // -- Allow for plan navigation, clicking anywhere on the layout brings you down a level -- //
        holder.layout.setOnClickListener((View v) -> m_displayChildrenPlansOfID.apply(p));

        // -- Only display the delete button if the level is editable -- //
        if (p.isEditable()) {
            holder.deleteButton.setOnClickListener((View v) -> m_deletePlan.apply(p));
        }
        // -- If the the current level can be scheduled, set the click even to allow changing of the schedule and neatly display the time -- //
        if (p.isSchedulable() && p instanceof PlanMeal) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, ((PlanMeal) p).getHour());
            c.set(Calendar.MINUTE, ((PlanMeal) p).getMinute());
            SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");

            holder.scheduleButton.setText("\uD83D\uDCC5 " + timeformat.format(c.getTime()));
            holder.scheduleButton.setOnClickListener((View v) -> {
                m_updatePlan.apply(p);
            });
        }

        // -- Update the visibility of all the additional features -- //
        holder.selectButton.setVisibility(View.GONE);
        holder.deleteButton.setVisibility((p.isEditable()) ? View.VISIBLE : View.GONE);
        holder.orderButton.setVisibility((p.isDraggable()) ? View.VISIBLE : View.GONE);
        holder.scheduleButton.setVisibility((p.isSchedulable()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return (m_displayPlans != null) ? m_displayPlans.size() : 0;
    }
}
