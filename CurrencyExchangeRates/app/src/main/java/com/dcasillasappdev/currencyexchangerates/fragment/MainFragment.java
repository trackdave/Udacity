package com.dcasillasappdev.currencyexchangerates.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.adapter.CurrencyCursorAdapter;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract;
import com.dcasillasappdev.currencyexchangerates.model.Currency;
import com.dcasillasappdev.currencyexchangerates.service.CurrencyReceiver;
import com.dcasillasappdev.currencyexchangerates.service.CurrencyService;
import com.dcasillasappdev.currencyexchangerates.utils.AlarmUtils;
import com.dcasillasappdev.currencyexchangerates.utils.Constants;
import com.dcasillasappdev.currencyexchangerates.utils.Utils;
import com.dcasillasappdev.currencyexchangerates.utils.WebUtils;
import com.dcasillasappdev.currencyexchangerates.view.HistoryActivity;
import com.dcasillasappdev.currencyexchangerates.view.SettingsActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface TargetListener {
        void onItemSelected(Bundle args);
    }

    private TargetListener listener;

    public static final int CURRENCY_LOADER = 0;

    private String baseCode;
    private String baseName;
    private List<Currency> currencies;
    public CurrencyCursorAdapter adapter;
    private CurrencyReceiver receiver;

    public MainFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TargetListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        adapter = null;
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        receiver = new CurrencyReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(Constants.NOTIFICATION));
        currencies = new ArrayList<>();
        adapter = new CurrencyCursorAdapter(getActivity(), null, 0);
        setListAdapter(adapter);
        /*mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                //.addApi()
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();*/
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getString(R.string.empty_loading));
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView baseTextView = (TextView) getActivity().findViewById(R.id.baseTextView);
        baseCode = Utils.getBaseCurrency(getActivity());
        baseName = Utils.getCurrencyName(baseCode);
        baseTextView.setText(baseName);
        if (Utils.getFrequency(getActivity()).equals(getString(R.string.daily)) && savedInstanceState == null) {
            startCurrencyService();
        } else {
            AlarmUtils.stopAlarmService();
        }
        getLoaderManager().initLoader(CURRENCY_LOADER, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) l.getItemAtPosition(position);
        if (cursor != null) {
            Bundle args = new Bundle();
            args.putString(Constants.KEY_BASE, baseName);
            args.putString(Constants.KEY_TARGET, cursor.getString(Constants.COLUMN_TARGET));
            listener.onItemSelected(args);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_refresh:
                manualRefresh();
                return true;
            case R.id.action_history:
                Intent historyIntent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(historyIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startCurrencyService() {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), CurrencyService.class);
        String baseCode = Utils.getBaseCurrency(getActivity());
        boolean notification = Utils.hasNotifications(getActivity());
        Bundle args = new Bundle();
        args.putString(Constants.KEY_BASE, baseCode);
        args.putBoolean(Constants.KEY_NOTIFICATION, notification);
        intent.putExtras(args);
        AlarmUtils.startAlarmService(getActivity(), intent);
    }

    private void manualRefresh() {
        String url = WebUtils.getLatestRate(baseCode);
        new ManualRefreshTask().execute(url);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(CURRENCY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String baseCode = Utils.getBaseCurrency(getActivity());
        String date = Utils.getCurrentDate(Constants.DATE_FORMAT);
        return new CursorLoader(getActivity(),
                CurrencyContract.CurrencyEntry.CONTENT_URI,
                Constants.CURRENCY_COLUMNS,
                Constants.sBaseDateSelection,
                new String[]{baseCode, date},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        if (!data.moveToFirst()) {
            setEmptyText(getString(R.string.empty_text));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private class ManualRefreshTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (WebUtils.hasInternetConnection(getActivity())) {
                JSONObject jsonObject = WebUtils.getJSONObject(params[0]);
                String date;
                if (jsonObject == null) {
                    return (getString(R.string.toast_website_down));
                }
                if (Utils.isWeekend()) {
                    date = Utils.getCurrentDate(Constants.DATE_FORMAT);
                } else {
                    date = jsonObject.optString(Constants.JSON_DATE);
                }
                currencies = Utils.getCurrenciesFromUrl(jsonObject, date);
                if (Utils.addCurrenciesToDatabase(getActivity(), currencies)) {
                    return getString(R.string.toast_update, baseName);
                } else {
                    return getString(R.string.toast_no_update);
                }
            } else {
                return getString(R.string.toast_connection_error);
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if (message.equals(getString(R.string.toast_update, baseName))) {
                restartLoader();
            }
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}