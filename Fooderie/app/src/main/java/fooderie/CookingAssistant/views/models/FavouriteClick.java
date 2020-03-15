package fooderie.CookingAssistant.views.models;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import fooderie.CookingAssistant.views.views.CookingAssistantViewer;
import fooderie.recipeBrowser.models.Recipe;
import fooderie.recipeBrowser.rbDisplay;
import fooderie.recipeBrowser.viewModels.RBViewModel;

public class FavouriteClick
{
    Recipe selected;
    RBViewModel rbViewModel;

    public FavouriteClick(Recipe s, RBViewModel rbVM)   //constructor
    {
        this.selected = s;
        this.rbViewModel = rbVM;
    }

    //Favourite button clicked
    public String favouriteClicked()
    {
        Recipe recipeIfExists = FetchRecipe(selected.url);
        Recipe favIfExists = FetchFav(selected.url);

        //It's in the db
        if(recipeIfExists != null)
        {
            //And it's not a fav
            if(favIfExists == null)
            {
                //update value - true
                selected.favorite = true;
                rbViewModel.updateRecipeFav(selected.url, selected.favorite);
                return "Added to Favourites";
            }
            //And it's a fav
            else
            {
                //update value - false
                selected.favorite = false;
                rbViewModel.updateRecipeFav(selected.url, selected.favorite);
                return "Deleted from Favourites";
            }
        }
        //It's not in the db
        else
        {
            //insert into db as a fav
            selected.favorite = true;
            rbViewModel.insert(selected);
            return "Added to Favourites";
        }
    }

    //Async task override to get favourites
    private class GetRecipeFromFavsAsyncTask extends AsyncTask<String, Void, Recipe>
    {
        @Override
        protected Recipe doInBackground(String... strings)
        {
            Recipe r = null;
            int count = strings == null ? 0 : strings.length;
            for(int i = 0; i < count; i++)
                r = rbViewModel.getFav(strings[i]);

            return r;
        }
    }

    //Async task override to get recipe
    private class GetRecipeAsyncTask extends AsyncTask<String, Void, Recipe>{
        @Override
        protected Recipe doInBackground(String... strings)
        {
            Recipe r = null;
            int count = strings == null ? 0 : strings.length;
            for(int i = 0; i < count; i++)
                r = rbViewModel.getRecipe(strings[i]);

            return r;
        }
    }

    //Fetch the favourite
    public Recipe FetchFav(String url)
    {
        GetRecipeFromFavsAsyncTask task = new GetRecipeFromFavsAsyncTask();
        task.execute(url);

        Recipe r = null;
        try
        {
            r = task.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return r;
    }

    //Fetches the recipe
    public Recipe FetchRecipe(String url)
    {
        GetRecipeAsyncTask task = new GetRecipeAsyncTask();
        task.execute(url);

        Recipe r = null;
        try
        {
            r = task.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return r;
    }




}
