package fooderie.models;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import fooderie.mealPlanner.models.Plan;
import fooderie.mealPlanner.models.PlanRecipe;

@Database(entities = {Plan.class, PlanRecipe.class, Recipe.class},
        version = 1,
        exportSchema = false)
public abstract class FooderieRoomDatabase extends RoomDatabase {
    abstract FooderieDao fooderieDao();

    private static volatile FooderieRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FooderieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FooderieRoomDatabase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FooderieRoomDatabase.class, "fooderie_database").addCallback(roomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //TODO: ENSURE THIS BLOCK IS DELETED WHEN WE NEED DATABASE TO PERSIST
            databaseWriteExecutor.execute(() -> {
                FooderieDao dao = INSTANCE.fooderieDao();
                dao.deleteAllPlans();
                dao.deleteAllPlanRecipes();

                Plan p = new Plan(null,0,"THIS IS A TEST");
                Plan p1 = new Plan(null,0,"THIS IS NOT A TEST");
                Plan p2 = new Plan(null,0,"THIS IS A MAYBE TEST");
                dao.insert(p);
                dao.insert(p1);
                dao.insert(p2);
            });
        }
    };
}
