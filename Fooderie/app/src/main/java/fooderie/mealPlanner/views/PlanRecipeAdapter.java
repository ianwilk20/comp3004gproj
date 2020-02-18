package fooderie.mealPlanner.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.models.Recipe;

public class PlanRecipeAdapter extends RecyclerView.Adapter<PlanRecipeAdapter.PlanRecipeViewHolder>{
    class PlanRecipeViewHolder extends RecyclerView.ViewHolder implements Target {
        private final TextView title;
        private final ConstraintLayout layout;
        private PlanRecipeViewHolder(View item) {
            super(item);
            title = item.findViewById(R.id.planRecipeItemTitle);
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

    PlanRecipeAdapter(Context context, Resources resources) {
        m_inflater = LayoutInflater.from(context);
        this.m_resources = resources;
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
            holder.title.setText("TEMP");
            Picasso.get().load("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png").into(holder);
        }
    }

    @Override
    public int getItemCount() {
        return (m_displayRecipes != null) ? m_displayRecipes.size() : 0;
    }
}
