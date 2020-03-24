package fooderie.mealPlanner.models;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Ignore;
import fooderie._main.models.FooderieRepository;

public class PlanRoot extends Plan {
    @Ignore
    static final PropertiesForPlan properties = new PropertiesForPlan();

    PlanRoot(PropertiesForPlan rootProperties, PropertiesForPlan rootPropertiesOfChild) {
        super(null, null, 0, rootProperties, rootPropertiesOfChild);
        this.planId = null;
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        setLiveDataHelper(repo.getWeekPlans(), owner, o);
    }
}
