package com.dcasillasappdev.currencyexchangerates.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.fragment.ChartFragment;
import com.dcasillasappdev.currencyexchangerates.fragment.MainFragment;
import com.dcasillasappdev.currencyexchangerates.utils.MyApplication;

public class MainActivity extends AppCompatActivity implements MainFragment.TargetListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).startTracking();

        if (findViewById(R.id.chart_container) != null) {
            mTwoPane = true;
            MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mainFragment);
            mainFragment.setActivateOnItemClick(true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onItemSelected(Bundle args) {
        if (mTwoPane) {
            ChartFragment fragment = new ChartFragment();
            fragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.chart_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }
}