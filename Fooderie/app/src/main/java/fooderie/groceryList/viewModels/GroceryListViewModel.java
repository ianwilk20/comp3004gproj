package fooderie.groceryList.viewModels;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import fooderie.groceryList.models.Food;
import fooderie.models.FooderieRepository;
import fooderie.groceryList.models.UserGroceryListItem;

public class GroceryListViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;
    private LiveData<List<UserGroceryListItem>> allGroceryItems;
    private LiveData<List<Food>> allFoodItems;

    public GroceryListViewModel(Application application){
        super(application);
        m_repo = new FooderieRepository(application);
        allGroceryItems = m_repo.getAllGroceryItems();
        allFoodItems = m_repo.getAllAPIIngredients();
    }

    public void insert(UserGroceryListItem item){
        m_repo.insert(item);
    }

    public void insert(Food f){
        m_repo.insert(f);
    }

    public void update(UserGroceryListItem item){
        m_repo.update(item);
    }

    public void update(Food f){
        m_repo.update(f);
    }

    public void updateGroceryItemAttributes(String prevName, String newName, String quantity, String notes, String department) {
        m_repo.updateGroceryItemAttributes(prevName, newName, quantity, notes, department);}

    public void delete(UserGroceryListItem item){
        m_repo.delete(item);
    }
    public void deleteAllGroceryItems() {m_repo.deleteAllGroceryItems();}
    public void deleteGroceryItemByName(String ingredientName) {m_repo.deleteGroceryItemByName(ingredientName);}
    public void delete(Food f){m_repo.delete(f);}
    public void deleteAllFoodFromAPI(){m_repo.deleteAllFoodFromAPI();}

    public LiveData<List<UserGroceryListItem>> getUserGroceryList() {return allGroceryItems;}
    public LiveData<List<Food>> getAllFoodItems() {return  allFoodItems;}
    public LiveData<Food> getFoodByID(String id) {return m_repo.getFoodByID(id);}
    public LiveData<List<Food>> getFoodByLabel(String label) {
        String alteredLabel = label + "%"; //allow to find all ingredients starting with the string specifed by label
        return m_repo.getFoodByLabel(alteredLabel);
    }


}
