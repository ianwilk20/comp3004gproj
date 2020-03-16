package fooderie.groceryList;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton vInstance;
    private RequestQueue vRequestQueue;

    private VolleySingleton(Context theContext){
        vRequestQueue = Volley.newRequestQueue(theContext.getApplicationContext());
    }

    //If no instance, of our volley singleton, will create a new one by calling above constructor and returns it
    //Otherwise, will give back the one already created and return it
    //Only possible to create one Singleton
    //Synchronized so only one thread can access this method (making sure only one Singleton can be created)
    public static synchronized VolleySingleton getInstance(Context theContext){
        if (vInstance == null){
            vInstance = new VolleySingleton(theContext);
        }
        return vInstance;
    }

    public RequestQueue getRequestQueue(){
        return vRequestQueue;
    }
}
