package com.scarecrow.root.orchardapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by root on 17-8-10.
 */

public class LeakDetecter extends Application {
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Application application) {
        LeakDetecter leakDetecter = (LeakDetecter) application;
        return leakDetecter.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

}
