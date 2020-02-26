package fooderie.mealPlanner.viewModels;

import android.app.Application;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.models.FooderieRepository;
import fooderie.recipeBrowser.models.Recipe;

public class TodayMealViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;
    private List<LiveData<List<Recipe>>> m_recipeLiveData;

    public TodayMealViewModel(Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
        m_recipeLiveData = new ArrayList<>();
    }

    public LiveData<List<Recipe>> getRecipesFromPlanMeal(PlanMeal p) {
        m_recipeLiveData.add(m_repo.getRecipes(p.getPlanId()));
        return m_recipeLiveData.get(m_recipeLiveData.size()-1);
    }

    public void clearRecipeLiveData(LifecycleOwner owner) {
        for (LiveData<List<Recipe>> l : m_recipeLiveData) {
            l.removeObservers(owner);
        }
        m_recipeLiveData = new ArrayList<>();
    }


    public LiveData<List<PlanMeal>> getMealsForToday() {
        Calendar calender = Calendar.getInstance();
        Long weekNum = (Long) Integer.toUnsignedLong(calender.get(Calendar.WEEK_OF_YEAR));

        return m_repo.getMealPlans(weekNum, dayOfWeekName());
    }

    public String dayOfWeekName() {
        Calendar calender = Calendar.getInstance();
        int dayNum = calender.get(Calendar.DAY_OF_WEEK);

        String[] names = new DateFormatSymbols().getWeekdays();
        return names[dayNum];
    }
}
