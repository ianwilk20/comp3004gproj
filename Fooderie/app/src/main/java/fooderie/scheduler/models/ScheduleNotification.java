package fooderie.scheduler.models;

import java.util.Date;

import androidx.annotation.NonNull;

public class ScheduleNotification {
    private Date date;
    private String title;
    private int recipeCount;

    public Date getDate() { return date; }
    public String getTitle() {return title; }
    public int getRecipeCount() { return recipeCount; }

    public ScheduleNotification(Date d, String t, int c) {
        date = d;
        title = t;
        recipeCount = c;
    }

    @Override
    public @NonNull String toString() {
        return date.toString() + " / " + title + " / " + Integer.toString(recipeCount);
    }
}
