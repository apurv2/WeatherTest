package com.apurv.weathertest.util;

import android.app.Application;
import android.content.Context;

/**
 * Singleton Class to return Application context
 * Created by apurv on 6/3/15.
 */
public class GlobalApplicationContext extends Application{

    private static GlobalApplicationContext sInstance;

    /**
     * lifecycle method that stores application context in mInstnce
     */
    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    /**
     * returns static instance variable.
     * @return
     */
    public static GlobalApplicationContext getsInstance()
    {
        return sInstance;

    }

    /**
     * returns application context from static instance variable
     * @return
     */
    public static Context getAppContext()
    {
        return sInstance.getApplicationContext();
    }
}
