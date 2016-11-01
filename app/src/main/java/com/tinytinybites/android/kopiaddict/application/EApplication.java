package com.tinytinybites.android.kopiaddict.application;

import android.app.Application;

/**
 * Created by bundee on 11/1/16.
 */
public class EApplication extends Application{
    //Tag
    protected static final String TAG = EApplication.class.getSimpleName();

    //Variables
    protected static EApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
    }

    /**
     * Get application instance
     */
    public static EApplication getInstance(){
        //Note: Application class started when application starts, _instance should never be null.
        return _instance;
    }


}
