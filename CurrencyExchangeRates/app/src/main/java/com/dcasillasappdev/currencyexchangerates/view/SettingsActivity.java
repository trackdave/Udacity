package com.dcasillasappdev.currencyexchangerates.view;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import com.dcasillasappdev.currencyexchangerates.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity{

    public static final String PREF_BASE = "pref_base";
    public static final String PREF_FREQUENCY = "pref_freq";
    public static final String PREF_NOTIFICATION = "pref_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // displays the settings fragment
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}