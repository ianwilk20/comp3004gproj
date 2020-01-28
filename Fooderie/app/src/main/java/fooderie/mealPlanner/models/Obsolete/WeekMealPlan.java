package fooderie.mealPlanner.models.Obsolete;

import java.io.IOError;
import java.util.ArrayList;

import fooderie.models.Recipe;

public class WeekMealPlan extends Planner<DayMealPlan>{
    public WeekMealPlan() {
        for (WeekDay c : WeekDay.values()) {
            DayMealPlan tmp = new DayMealPlan(c.toString());
            subPlans.add(tmp);
        }
    }

    public boolean addMeal(Meal meal, WeekDay w) {
        for (DayMealPlan c : subPlans) {
            if (w.toString().equals(c.name)) {
                return c.subPlans.add(meal);
            }
        }
        return false;
    }

    public boolean addRecipe(Recipe recipe, WeekDay w, Meal meal) {
        for (DayMealPlan c : subPlans) {
            if (w.toString().equals(c.name)) {

                for (Meal m : c.subPlans) {
                    if (m.name.equals(meal.name)) {
                        return m.subPlans.add(recipe);
                    }
                }
            }
        }
        return false;
    }

    /*public void swapMeal(DayMealPlan dayMealPlan, Meal a, Meal b) {

    }

    public void swapRecipe(DayMealPlan dayMealPlan, Meal meal, Recipe a, Recipe b) {

    }*/

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        for (DayMealPlan dayMealPlan : this.subPlans) {
            for (Meal meal : dayMealPlan.subPlans) {
                recipes.addAll(meal.subPlans);
            }
        }
        return recipes;
    }


    /* Valid WeekMealPlan contains:
    *   - a DayMealPlan for each day of the week
    *   - valid DayMealPlans for each day of the week
    * */
    @Override
    public boolean validate() {
        if (subPlans.size() != WeekDay.values().length)
            return false;

        for (WeekDay w : WeekDay.values()) {
            boolean flag = false;

            for (DayMealPlan c : subPlans) {
                if (w.toString().equals(c.name)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) return false;
        }

        for (DayMealPlan c : subPlans) {
            if (!c.validate())
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
