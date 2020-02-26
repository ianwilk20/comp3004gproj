package fooderie.mealPlanner.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooderie.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import fooderie.models.Recipe;

public class AdapterRecipe extends RecyclerView.Adapter<AdapterRecipe.PlanRecipeViewHolder> {
    class PlanRecipeViewHolder extends RecyclerView.ViewHolder implements Target {
        private final TextView title;
        private final ImageView deleteButton;
        private final ConstraintLayout layout;
        private PlanRecipeViewHolder(View item) {
            super(item);
            title = item.findViewById(R.id.planRecipeItemTitle);
            deleteButton = item.findViewById(R.id.planRecipeItemDeleteButton);
            layout = item.findViewById(R.id.planRecipeItem);
        }

        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            layout.setBackground(new BitmapDrawable(m_resources, bitmap));
        }

        @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            layout.setBackground(errorDrawable);
        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
            layout.setBackground(placeHolderDrawable);
        }
    }

    private final LayoutInflater m_inflater;
    private List<Recipe> m_displayRecipes;
    private Resources m_resources;
    private Function<String, Void> m_deleteFunc;
    private Function<Recipe, Void> m_displayFunc;

    AdapterRecipe(Context context, Resources resources, Function<String, Void> delete, Function<Recipe, Void> display) {
        m_inflater = LayoutInflater.from(context);
        m_resources = resources;
        m_deleteFunc = delete;
        m_displayFunc = display;
    }

    void setPlanRecipes(List<Recipe> recipes) {
        this.m_displayRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public @NonNull PlanRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = m_inflater.inflate(R.layout.planrecipe_recyclerview_item, parent, false);
        return new PlanRecipeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanRecipeViewHolder holder, int pos) {
        if (m_displayRecipes != null) {
            Recipe r = m_displayRecipes.get(pos);

            holder.title.setText(r.label);
            Picasso.get().load(r.image).into(holder);

            holder.deleteButton.setOnClickListener((View v) -> {
                m_deleteFunc.apply(r.getId());
            });
            holder.layout.setOnClickListener((View v) -> {
                m_displayFunc.apply(r);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (m_displayRecipes != null) ? m_displayRecipes.size() : 0;
    }
}
