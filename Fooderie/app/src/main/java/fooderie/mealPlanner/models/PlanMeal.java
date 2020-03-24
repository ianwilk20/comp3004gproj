package fooderie.mealPlanner.models;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import fooderie._main.models.FooderieRepository;

@Entity(tableName = "table_PlanMeal",
        indices = {@Index("planId"), @Index("parentId")},
        foreignKeys = @ForeignKey(entity = PlanDay.class,
                parentColumns = "planId",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE)
)
public class PlanMeal extends Plan implements Comparable<PlanMeal>{
    private int order;
    private int hour;
    private int minute;

    @Ignore
    private static final String planName = "Meal Plan";
    @Ignore
    static final PropertiesForPlan properties = new PropertiesForPlan(true, true, true, planName);

    public PlanMeal(Long parentId, String name, int recipeCount, int order) {
        super(parentId, name, recipeCount, properties, PlanRecipe.properties);
        this.order = order;
        this.hour = 12;
        this.minute = 0;
    }

    @Override
    public void setLiveData(FooderieRepository repo, LifecycleOwner owner, Observer o) {
        setLiveDataHelper(repo.getRecipes(planId), owner, o);
    }

    public void setOrder(int order) {this.order = order;}
    public int getOrder() { return order;}
    public void setHour(int hour) {this.hour = hour;}
    public int getHour() {return hour;}
    public void setMinute(int minute) {this.minute = minute;}
    public int getMinute() {return minute;}

    @Override
    public int compareTo(PlanMeal a) {
        return order - a.order;
    }
}
