package com.applicationlist;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Vikrant Chauhan on 10/14/2016.
 */

public class ListApplication extends Application {

    public final static String API_KEY = "7e8f60e325cd06e164799af1e317d7a7";

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
