package com.dcasillasappdev.currencyexchangerates.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.widget.Spinner;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyDBHelper;
import com.dcasillasappdev.currencyexchangerates.model.Currency;
import com.dcasillasappdev.currencyexchangerates.view.SettingsActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String getBaseCurrency(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SettingsActivity.PREF_BASE, "USD");
    }

    public static String getFrequency(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SettingsActivity.PREF_FREQUENCY, context.getString(R.string.daily));
    }

    public static boolean hasNotifications(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SettingsActivity.PREF_NOTIFICATION, false);
    }

    public static String getCurrencyCode(String name) {
        List<String> currencyNames = new ArrayList<>(Arrays.asList(Constants.CURRENCY_NAMES));
        int position = currencyNames.indexOf(name);
        return Constants.CURRENCY_CODES[position];
    }

    public static String getCurrencyName(String code) {
        List<String> currencyCodes = new ArrayList<>(Arrays.asList(Constants.CURRENCY_CODES));
        int position = currencyCodes.indexOf(code);
        return Constants.CURRENCY_NAMES[position];
    }

    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        return DateFormat.format(format, calendar.getTime()).toString();
    }

    public static boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static boolean hasBaseAndDate(Context context, String base, String date) {
        CurrencyDBHelper db = new CurrencyDBHelper(context);
        return db.hasBaseAndDate(base, date);
    }

    public static Currency parseCurrency(JSONObject jsonObject, String target, String date) {
        Currency currency = new Currency();
        currency.setBase(jsonObject.optString(Constants.JSON_BASE));
        currency.setTarget(target);
        currency.setDate(date);
        JSONObject jsonRates = jsonObject.optJSONObject(Constants.JSON_RATES);
        currency.setRate(jsonRates.optDouble(getCurrencyCode(target), 0.0));
        return currency;
    }

    public static Vector<ContentValues> parseCurrencyValues(List<Currency> currencies) {
        Vector<ContentValues> cVVector = new Vector<>(currencies.size());
        for (Currency currency : currencies) {
            ContentValues currencyValues = new ContentValues();
            currencyValues.put(CurrencyContract.CurrencyEntry.COLUMN_BASE, currency.getBase());
            currencyValues.put(CurrencyContract.CurrencyEntry.COLUMN_TARGET, currency.getTarget());
            currencyValues.put(CurrencyContract.CurrencyEntry.COLUMN_RATE, currency.getRate());
            currencyValues.put(CurrencyContract.CurrencyEntry.COLUMN_DATE, currency.getDate());
            cVVector.add(currencyValues);
        }
        return cVVector;
    }

    public static boolean addCurrenciesToDatabase(Context context, List<Currency> currencies) {
        Currency currency = currencies.get(0);
        if (!hasBaseAndDate(context, currency.getBase(), currency.getDate())) {
            Vector<ContentValues> cVVector = Utils.parseCurrencyValues(currencies);
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                context.getContentResolver().bulkInsert(CurrencyContract.CurrencyEntry.CONTENT_URI, cvArray);
            }
            return true;
        } else {
            return false;
        }
    }

    public static List<Currency> getCurrenciesFromUrl(JSONObject jsonObject, String date) {
        List<Currency> currencies = new ArrayList<>();
        for (String target : Constants.CURRENCY_NAMES) {
            Currency currency = Utils.parseCurrency(jsonObject, target, date);
            if (currency.getRate() != 0.0)
                currencies.add(currency);
        }
        return currencies;
    }

    public static void setSpinner(Spinner spinner, String baseName) {
        int index = -1;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (baseName.equals(spinner.getItemAtPosition(i))) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }
}