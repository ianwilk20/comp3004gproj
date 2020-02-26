package fooderie.mealPlanner.viewModels;

import android.app.Application;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.models.FooderieRepository;

public class TodayMealViewModel extends AndroidViewModel {
    private FooderieRepository m_repo;

    public TodayMealViewModel(Application application) {
        super(application);
        m_repo = new FooderieRepository(application);
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
