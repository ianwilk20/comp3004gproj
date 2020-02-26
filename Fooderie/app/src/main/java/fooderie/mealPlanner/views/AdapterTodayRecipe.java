package fooderie.mealPlanner.views;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooderie.R;

import fooderie.models.Recipe;

import java.util.List;

public class AdapterTodayRecipe extends RecyclerView.Adapter<AdapterTodayRecipe.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final ImageView mImage;

        private ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.TodayRecipeTitle);
            mImage = view.findViewById(R.id.TodayRecipeImage);
        }
    }


    private final List<Recipe> mRecipes;

    public AdapterTodayRecipe(List<Recipe> items) {
        mRecipes = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_todaymeal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipe r = mRecipes.get(position);

        //holder.mTitle.setText(p.getName());
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}
