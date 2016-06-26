package com.dcasillasappdev.currencyexchangerates.view;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.fragment.ChartFragment;

public class ChartActivity extends AppCompatActivity {

    private static final String TAG = ChartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        if (savedInstanceState == null) {
            ChartFragment chartFragment = new ChartFragment();
            chartFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chart_container, chartFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}