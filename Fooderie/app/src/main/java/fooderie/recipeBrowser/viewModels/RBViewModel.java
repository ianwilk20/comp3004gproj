package fooderie.recipeBrowser.viewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;
import fooderie.models.FooderieRepository;
import fooderie.recipeBrowser.models.Recipe;

public class RBViewModel extends AndroidViewModel {
    private FooderieRepository repo;

    public RBViewModel(Application app){
        super(app);
        repo = new FooderieRepository(app);
    }

    public void insert(Recipe r){repo.insert(r);}
    public void delete(Recipe r){repo.delete(r);}
    public void deleteAllRecipes(){repo.deleteAllRecipes();}
    public void updateRecipeFav(String url, Boolean value){repo.updateRecipeFav(url, value);}
    public List<Recipe> getAllRecipes(){return repo.getAllRecipes();}
    public List<Recipe> getAllFavs(){return repo.getAllFavs();}
    public Recipe getRecipe(String url){return repo.getRecipe(url);}
    public Recipe getFav(String url){return repo.getFav(url);}
}
