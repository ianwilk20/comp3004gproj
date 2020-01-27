package fooderie.mealPlanner.models;

import java.io.IOError;
import java.util.ArrayList;

public abstract class Planner<T> {
    protected int id;
    protected String name;
    ArrayList<T> subPlans = new ArrayList<>();

    public abstract boolean validate();
    public abstract void save() throws IOError;
    public abstract void load() throws IOError;
}
