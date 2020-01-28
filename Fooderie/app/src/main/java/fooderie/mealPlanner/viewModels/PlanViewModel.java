package fooderie.mealPlanner.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import fooderie.models.FooderieRepository;

public class PlanViewModel extends AndroidViewModel {
    private FooderieRepository repo;

    public PlanViewModel (Application application) {
        super(application);
    }
}
