package com.dcasillasappdev.currencyexchangerates.utils;


import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract;

public class Constants {

    /* Fixer.io is a free JSON API for current and historical foreign exchange rates published by the European Central Bank.
     * The rates are updated daily around 3PM CET */

    public static final String URL = "http://api.fixer.io/";
    public static final String LATEST = "latest";
    public static final String BASE = "base";

    public static final String[] CURRENCY_CODES = {
            "AUD", "RUB", "BRL", "BGN", "CAD", "HRK", "CZK", "DKK", "EUR", "HKD", "HUF", "INR",
            "IDR", "ILS", "JPY", "KRW", "MYR", "MXN", "NZD", "NOK", "PHP", "PLN", "GBP", "RON",
            "SGD", "ZAR", "SEK", "CHF", "THB", "TRY", "USD", "CNY"
    };

    public static final String[] CURRENCY_NAMES = {
            "Australian Dollar", "Belarussian Ruble", "Bulgarian Lev", "Brazilian Real",
            "Canadian Dollar", "Croatian Kuna", "Czech Koruna", "Danish Krone", "Euro",
            "Hong Kong Dollar", "Hungarian Forint", "Indian Rupee", "Indonesian Rupiah",
            "Israeli New Shekel", "Japanese Yen", "Korean Won", "Malaysian Ringgit",
            "Mexican Nuevo Peso", "New Zealand Dollar", "Norwegian Krone", "Philippine Peso",
            "Polish Zloty", "Pound Sterling", "Romanian New Leu", "Singapore Dollar",
            "South African Rand", "Swedish Krona", "Swiss Franc", "Thai Baht", "Turkish Lira",
            "US Dollar", "Yuan Renminbi"
    };

    public static final String JSON_BASE = "base";
    public static final String JSON_DATE = "date";
    public static final String JSON_RATES = "rates";

    public static final String KEY_CURRENCIES = "key_currencies";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_BASE = "key_base";
    public static final String KEY_TARGET = "key_target";
    public static final String KEY_NOTIFICATION = "key_notification";

    public static final String sBaseDateSelection =
            CurrencyContract.CurrencyEntry.TABLE_NAME
                    + "." + CurrencyContract.CurrencyEntry.COLUMN_BASE + " =? AND "
                    + CurrencyContract.CurrencyEntry.TABLE_NAME
                    + "." + CurrencyContract.CurrencyEntry.COLUMN_DATE + " =?";

    public static final String sBaseTargetSelection =
            CurrencyContract.CurrencyEntry.TABLE_NAME
                    + "." + CurrencyContract.CurrencyEntry.COLUMN_BASE + " =? AND "
                    + CurrencyContract.CurrencyEntry.TABLE_NAME
                    + "." + CurrencyContract.CurrencyEntry.COLUMN_TARGET + " =? ORDER BY "
                    + CurrencyContract.CurrencyEntry.TABLE_NAME
                    + "." + CurrencyContract.CurrencyEntry.COLUMN_DATE + " ASC";

    public static final String[] CURRENCY_COLUMNS = {
            CurrencyContract.CurrencyEntry.TABLE_NAME + "." + CurrencyContract.CurrencyEntry._ID,
            CurrencyContract.CurrencyEntry.COLUMN_BASE,
            CurrencyContract.CurrencyEntry.COLUMN_TARGET,
            CurrencyContract.CurrencyEntry.COLUMN_RATE,
            CurrencyContract.CurrencyEntry.COLUMN_DATE
    };


    public static final int COLUMN_TARGET = 2;
    public static final int COLUMN_RATE = 3;
    public static final int COLUMN_DATE = 4;

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_LONG = "MMM dd, yyyy";

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;

    public static final String RESULT = "result";
    public static final String MESSAGE = "message";
    public static final String NOTIFICATION = "com.dcasillasappdev.currencyrates.currencyservice";

    public static final int NOTIFICATION_ID = 99;

}