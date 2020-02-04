package fooderie.mealPlanner.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooderie.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fooderie.mealPlanner.models.Plan;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder>{
    public class PlanViewHolder extends RecyclerView.ViewHolder {
        private final TextView view;
        private PlanViewHolder(View item) {
            super(item);
            view = item.findViewById(R.id.PlanView);
        }
    }

    private final LayoutInflater inflater;
    private List<Plan> plans;

    public PlanAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.plan_recyclerview_item, parent, false);
        return new PlanViewHolder(item);
    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int pos) {
        if (plans != null) {
            Plan current = plans.get(pos);
            holder.view.setText(current.getName());
        } else {
            holder.view.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return (plans != null) ? plans.size() : 0;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }


}
