package test.demo.com.chatmodule.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import test.demo.com.chatmodule.database.DatabaseManager;

/**
 * Created by phoosaram on 3/6/2017.
 */

public class ChatApplication extends Application {
    private static RequestQueue requestQueue;
    private static Context context;
    public static final String FROM_USER_ID = "1";
    public static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            context = getApplicationContext();
            DatabaseManager.getInstance(context);
        } catch (Exception   e) {
            e.printStackTrace();
        }
    }

    /**
     * method which returns volley request queue
     *
     * @return
     */
    public static RequestQueue getVolleyRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

}
