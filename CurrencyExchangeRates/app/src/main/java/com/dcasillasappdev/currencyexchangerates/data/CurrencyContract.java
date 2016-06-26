package com.dcasillasappdev.currencyexchangerates.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CurrencyContract {

    public static final String CONTENT_AUTHORITY = "com.dcasillasappdev.currencyexchangerates";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CURRENCY = "currency";

    public static final class CurrencyEntry implements BaseColumns {

        public static final String TABLE_NAME = "currency";
        public static final String COLUMN_BASE = "base";
        public static final String COLUMN_TARGET = "target";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_DATE = "date";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CURRENCY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CURRENCY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CURRENCY;

        public static Uri buildCurrencyUri(long id) {
            return ContentUris.withAppendedId(BASE_CONTENT_URI, id);
        }
    }
}