package com.ph;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Anup on 1/11/2016.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
