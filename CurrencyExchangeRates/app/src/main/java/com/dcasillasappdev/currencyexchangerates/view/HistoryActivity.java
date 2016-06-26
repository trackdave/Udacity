package com.dcasillasappdev.currencyexchangerates.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.adapter.CurrencyListAdapter;
import com.dcasillasappdev.currencyexchangerates.fragment.DatePickerFragment;
import com.dcasillasappdev.currencyexchangerates.model.Currency;
import com.dcasillasappdev.currencyexchangerates.utils.Constants;
import com.dcasillasappdev.currencyexchangerates.utils.Utils;
import com.dcasillasappdev.currencyexchangerates.utils.WebUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity
        implements DatePickerFragment.DatePickerListener {


    private static final String TAG = HistoryActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private ListView listView;
    private Spinner spinner;
    private TextView dateTextView;
    private List<Currency> currencies;
    private CurrencyListAdapter adapter;
    private String date;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currencies.size() != 0) {
            outState.putParcelableArrayList(Constants.KEY_CURRENCIES, new ArrayList<Parcelable>(currencies));
            outState.putString(Constants.KEY_DATE, dateTextView.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.historyListView);
        spinner = (Spinner) findViewById(R.id.spinner);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        currencies = new ArrayList<>();

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.currency_names,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        Utils.setSpinner(spinner, Utils.getCurrencyName(Utils.getBaseCurrency(this)));

        adapter = new CurrencyListAdapter(this);
        listView.setEmptyView(findViewById(android.R.id.empty));

        if (savedInstanceState != null) {
            dateTextView.setText(savedInstanceState.getString(Constants.KEY_DATE));
            currencies = savedInstanceState.getParcelableArrayList(Constants.KEY_CURRENCIES);
            adapter.setCurrencyList(currencies);
            listView.setAdapter(adapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), null);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveRates();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void saveRates() {
        if (currencies.size() == 0) {
            Toast.makeText(getApplicationContext(), R.string.toast_no_data, Toast.LENGTH_SHORT).show();
        } else {
            new GetExchangeRates(GetExchangeRates.DOWNLOAD).execute();
        }
    }

    @Override
    public void getRates(Calendar calendar) {
        String base = Utils.getCurrencyCode(spinner.getSelectedItem().toString());
        date = DateFormat.format(Constants.DATE_FORMAT, calendar.getTime()).toString();
        String url = WebUtils.getRateByDate(date, base);
        String longDate = DateFormat.format(Constants.DATE_FORMAT_LONG, calendar.getTime()).toString();
        dateTextView.setText(longDate);
        new GetExchangeRates(GetExchangeRates.RETRIEVE).execute(url);
    }

    private class GetExchangeRates extends AsyncTask<String, Void, String> {

        public static final int DOWNLOAD = 0;
        public static final int RETRIEVE = 1;

        private int method;

        public GetExchangeRates(int method) {
            this.method = method;
        }

        @Override
        protected void onPreExecute() {
            if (method == RETRIEVE)
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            if (WebUtils.hasInternetConnection(getApplicationContext())) {
                if (method == RETRIEVE) {
                    JSONObject jsonObject = WebUtils.getJSONObject(params[0]);
                    currencies = Utils.getCurrenciesFromUrl(jsonObject, date);
                    return null;
                }
                if (Utils.addCurrenciesToDatabase(getApplicationContext(), currencies)) {
                    return getString(R.string.toast_save_data);
                } else {
                    return getString(R.string.toast_already_saved_data);
                }
            } else {
                return getString(R.string.toast_connection_error);
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if (method == RETRIEVE) {
                progressBar.setVisibility(View.GONE);
                adapter.setCurrencyList(currencies);
                listView.setAdapter(adapter);
            }
            if (message != null) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}