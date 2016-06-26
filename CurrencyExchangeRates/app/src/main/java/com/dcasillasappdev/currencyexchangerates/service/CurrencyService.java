package com.dcasillasappdev.currencyexchangerates.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract;
import com.dcasillasappdev.currencyexchangerates.model.Currency;
import com.dcasillasappdev.currencyexchangerates.utils.Constants;
import com.dcasillasappdev.currencyexchangerates.utils.NotificationUtils;
import com.dcasillasappdev.currencyexchangerates.utils.Utils;
import com.dcasillasappdev.currencyexchangerates.utils.WebUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class CurrencyService extends IntentService{

    private static final String TAG = CurrencyService.class.getSimpleName();

    public CurrencyService() {
        super(TAG);
    }

    private String base;
    private boolean hasNotification;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle args = intent.getExtras();
        base = args.getString(Constants.KEY_BASE);
        hasNotification = args.getBoolean(Constants.KEY_NOTIFICATION);
        String date;
        if (WebUtils.hasInternetConnection(getApplicationContext())) {
            String url = WebUtils.getLatestRate(base);
            JSONObject jsonObject = WebUtils.getJSONObject(url);
            if (jsonObject == null) {
                sendMessage(getString(R.string.toast_website_down));
                return;
            }
            if (Utils.isWeekend()) {
                date = Utils.getCurrentDate(Constants.DATE_FORMAT);
            } else {
                date = jsonObject.optString(Constants.JSON_DATE);
            }
            if (Utils.hasBaseAndDate(getApplicationContext(), base, date)) { // Have data, do no not insert
                Log.d(TAG, getString(R.string.toast_already_saved_data));
            } else { // Insert new data
                List<Currency> currencies = Utils.getCurrenciesFromUrl(jsonObject, date);
                saveData(currencies);
                sendMessage(getString(R.string.toast_update, base));
            }
        } else {
            sendMessage(getString(R.string.toast_connection_error));
        }
        stopSelf();
    }

    private void sendMessage(String message) {
        Intent intent = new Intent(Constants.NOTIFICATION);
        Bundle args = new Bundle();
        args.putString(Constants.MESSAGE, message);
        intent.putExtras(args);
        sendBroadcast(intent);
    }

    private void saveData(List<Currency> currencies) {
        Vector<ContentValues> cVVector = Utils.parseCurrencyValues(currencies);
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContentResolver().bulkInsert(CurrencyContract.CurrencyEntry.CONTENT_URI, cvArray);
            if (hasNotification) {
                NotificationUtils.showNotification(getApplicationContext(), getString(R.string.app_name),
                        getString(R.string.toast_update, base));
            }
        }
    }

}