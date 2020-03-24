package fooderie.mealPlanner.models;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import fooderie._main.models.FooderieRepository;

@Entity(tableName = "table_PlanDay",
        indices = {@Index("planId"), @Index("parentId")},
        foreignKeys = @ForeignKey(entity = PlanWeek.class,
                parentColumns = "planId",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE)
)
public class PlanDay extends Plan {
    @Ignore
    private static final String planName = "Day Plan";
    @Ignore
    static final PropertiesForPlan properties = new PropertiesForPlan(false, false, false, planName);

    public PlanDay(Long parentId, String name, int recipeCount) {
        super(parentId, name, recipeCount, properties, PlanMeal.properties);
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        setLiveDataHelper(repo.getMealPlans(planId), owner, o);
    }
}
