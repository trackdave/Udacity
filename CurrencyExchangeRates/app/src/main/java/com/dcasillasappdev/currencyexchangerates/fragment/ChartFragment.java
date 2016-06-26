package com.dcasillasappdev.currencyexchangerates.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract;
import com.dcasillasappdev.currencyexchangerates.utils.Constants;
import com.dcasillasappdev.currencyexchangerates.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DecimalFormat;

public class ChartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ChartFragment.class.getSimpleName();

    private static final int CURRENCY_LOADER = 0;

    private String base;
    private String target;
    private LineChart mLineChart;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        base = args.getString(Constants.KEY_BASE);
        target = args.getString(Constants.KEY_TARGET);
        mInterstitialAd = newInterstitialAd();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        initChart(view);

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template")
                .build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURRENCY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void initChart(View view) {
        mLineChart = (LineChart) view.findViewById(R.id.lineChart);
        mLineChart.setNoDataText(getString(R.string.no_chart_data, base, target));
        mLineChart.setHighlightPerTapEnabled(true);
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setPinchZoom(true);
        mLineChart.setDescription("Line Chart Data for " + base + " vs " + target);
        mLineChart.setDescriptionTextSize(14f);

        LineData lineData = new LineData();
        lineData.setValueTextColor(getResources().getColor(R.color.colorAccent));
        mLineChart.setData(lineData);

        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(Color.BLACK);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.DKGRAY);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setDrawGridLines(true);
        yAxis.setGranularityEnabled(true);
        yAxis.setGridColor(Color.DKGRAY);
        yAxis.setDrawLabels(false);

        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private void setChartData(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(Constants.COLUMN_DATE);
                double rate = cursor.getDouble(Constants.COLUMN_RATE);
                addDataEntry(date, rate);
            } while (cursor.moveToNext());
        }
    }

    private void addDataEntry(String date, double rate) {
        LineData lineData = mLineChart.getData();
        if (lineData != null) {
            ILineDataSet lineDataSet = lineData.getDataSetByIndex(0);
            if (lineDataSet == null) {
                lineDataSet = createSet();
                lineData.addDataSet(lineDataSet);
            }

            if (!mLineChart.getData().getXVals().contains(date)) {
                lineData.addXValue(date);
            }
            lineData.addEntry(new Entry((float) rate, lineDataSet.getEntryCount()), 0);
            mLineChart.notifyDataSetChanged();
        }
        mLineChart.invalidate();
    }

    private LineDataSet createSet() {
        int color = getResources().getColor(R.color.colorAccent);
        LineDataSet lineDataSet = new LineDataSet(null, base + " vs " + target);
        lineDataSet.setDrawCubic(false);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setCircleSize(4f);
        lineDataSet.setFillColor(color);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setHighLightColor(color);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(8f);
        lineDataSet.setValueFormatter(new MyValueFormatter());
        return lineDataSet;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                CurrencyContract.CurrencyEntry.CONTENT_URI,
                Constants.CURRENCY_COLUMNS,
                Constants.sBaseTargetSelection,
                new String[]{Utils.getCurrencyCode(base), target},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setChartData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("@@@@@");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}