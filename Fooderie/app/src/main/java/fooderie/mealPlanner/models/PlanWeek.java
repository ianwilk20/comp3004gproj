package fooderie.mealPlanner.models;

import java.io.Serializable;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import fooderie.models.FooderieRepository;

@Entity(tableName = "table_PlanWeek",
        indices = {@Index("planId"), @Index("parentId")}
        )
public class PlanWeek extends Plan {
    @Ignore
    private static final String planName = "Week Plan";
    @Ignore
    static final PropertiesForPlan properties = new PropertiesForPlan(true, false, false, planName);

    public PlanWeek(String name, int recipeCount) {
        super(null, name, recipeCount, properties, PlanDay.properties);
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        setLiveDataHelper(repo.getDayPlans(planId), owner, o);
    }
}
