package fooderie.models;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanRecipe;

@Database(entities = {Plan.class, PlanRecipe.class, Recipe.class},
        version = 1)
abstract class FooderieRoomDatabase extends RoomDatabase {
    abstract FooderieDao fooderieDao();

    private static volatile FooderieRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FooderieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FooderieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FooderieRoomDatabase.class, "fooderie_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
