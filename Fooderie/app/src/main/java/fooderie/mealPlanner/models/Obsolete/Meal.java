package fooderie.mealPlanner.models.Obsolete;

import java.io.IOError;

import fooderie.models.Recipe;

public class Meal extends Planner<Recipe> {
    public Meal() {

    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public void save() throws IOError {

    }

    @Override
    public void load() throws IOError {

    }
}