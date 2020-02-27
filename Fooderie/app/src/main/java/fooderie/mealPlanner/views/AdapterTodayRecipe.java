package fooderie.mealPlanner.views;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.recipeBrowser.models.Recipe;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooderie.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.function.Function;

public class AdapterTodayRecipe extends RecyclerView.Adapter<AdapterTodayRecipe.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final ImageView mImage;
        private final ConstraintLayout mLayout;

        private ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.TodayRecipeTitle);
            mImage = view.findViewById(R.id.TodayRecipeImage);
            mLayout = view.findViewById(R.id.TodayRecipeLayout);
        }
    }

    private List<Recipe> mRecipes;
    private final Function<Recipe, Void> mDisplayRecipe;

    public void setRecipes(List<Recipe> r) {
        mRecipes = r;
        notifyDataSetChanged();
    }

    public AdapterTodayRecipe(Function<Recipe, Void> displayRecipe) {
        mDisplayRecipe = displayRecipe;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_todaymeal_recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipe r = mRecipes.get(position);

        holder.mTitle.setText(r.getLabel());
        Picasso.get().load(r.getImage()).into(holder.mImage);

        holder.mLayout.setOnClickListener((View v) -> {
            mDisplayRecipe.apply(r);
        });
    }

    @Override
    public int getItemCount() {
        return (mRecipes == null) ? 0 : mRecipes.size();
    }
}
