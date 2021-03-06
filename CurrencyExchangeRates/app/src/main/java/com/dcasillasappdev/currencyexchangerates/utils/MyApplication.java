package com.dcasillasappdev.currencyexchangerates.utils;

import android.app.Application;

import com.dcasillasappdev.currencyexchangerates.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class MyApplication extends Application {

    public Tracker mTracker;

    public void startTracking() {
        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker = ga.newTracker(R.xml.track_app);
            ga.enableAutoActivityReports(this);
        }
    }

    public Tracker getTracker() {
        startTracking();
        return mTracker;
    }

}