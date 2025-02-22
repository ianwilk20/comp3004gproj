package fooderie._main.models;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import fooderie.groceryList.models.Food;
import fooderie.groceryList.models.UserGroceryListItem;
import fooderie.mealPlanner.models.PlanDay;
import fooderie.mealPlanner.models.PlanMeal;
import fooderie.mealPlanner.models.PlanRecipe;
import fooderie.mealPlanner.models.PlanWeek;
import fooderie.scheduler.models.Schedule;
import fooderie.recipeBrowser.models.ArrayTypeConverter;
import fooderie.recipeBrowser.models.Recipe;
import fooderie.recipeBrowser.models.TotalNutrientsTypeConverter;


@Database(entities = {Schedule.class, PlanWeek.class, PlanDay.class, PlanMeal.class, PlanRecipe.class, Recipe.class, UserGroceryListItem.class, Food.class},
        version = 1,
        exportSchema = false)
@TypeConverters({ArrayTypeConverter.class, TotalNutrientsTypeConverter.class})
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
                            FooderieRoomDatabase.class, "fooderie_database")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                FooderieDao dao = INSTANCE.fooderieDao();
                /*List<Recipe> recipes = dao.getAllRecipes();
                if (recipes.size() == 0) {
                    Recipe r = new Recipe();
                    r.setId("0");
                    //r.setId(0L);
                    dao.insert(r);
                }*/

                // -- Populate schedules list if it is empty (first time running application) -- //
                List<Schedule> schedules = dao.getAllSchedulesNonLiveData();
                if (schedules.size() == 0) {
                    for (Long i = 1L; i < 53; i++) {
                        Schedule p = new Schedule(i, null);
                        dao.insert(p);
                    }
                }

                //dao.deleteAllRecipes();

                //Recipe r = new Recipe();
                //r.setId(0L);
                //dao.insert(r);


                //dao.deleteAllPlans();
                //Log.d("FooderieRoomDatabase", "onOpen: Everything finished deleting...I think");
                /*dao.deleteAllPlanRecipes();

                Plan p = new Plan(null,"THIS IS A TEST");
                Plan p1 = new Plan(null,"THIS IS NOT A TEST");
                Plan p2 = new Plan(null,"THIS IS A MAYBE TEST");
                dao.insert(p);
                dao.insert(p1);
                dao.insert(p2);*/
            });
        }
    };
}
