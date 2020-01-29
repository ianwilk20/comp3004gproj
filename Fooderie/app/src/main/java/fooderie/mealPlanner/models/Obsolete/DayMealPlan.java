package fooderie.mealPlanner.models.Obsolete;

import java.io.IOError;

public class DayMealPlan extends Planner<Meal> {
    DayMealPlan(String name) {
        this.name = name;
    }

    /* Valid DayMealPlan contains:
     *   - Any number of meals
     *   - valid meals
     * */
    @Override
    public boolean validate() {
        for (Meal c : subPlans) {
            if (c.validate())
                return false;
        }
        return true;
    }

    @Override
    public void save() throws IOError {

    }

    @Override
    public void load() throws IOError {

    }
}
