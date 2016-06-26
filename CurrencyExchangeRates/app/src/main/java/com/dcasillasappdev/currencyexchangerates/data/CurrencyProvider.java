package com.dcasillasappdev.currencyexchangerates.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract.CurrencyEntry;

public class CurrencyProvider extends ContentProvider {

    private static final String TAG = CurrencyProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CurrencyDBHelper mOpenHelper;

    private static final SQLiteQueryBuilder sCurrencyQueryBuilder;
    private static final int CURRENCY = 100;

    static {
        sCurrencyQueryBuilder = new SQLiteQueryBuilder();
        sCurrencyQueryBuilder.setTables(CurrencyEntry.TABLE_NAME);
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CurrencyContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, CurrencyContract.PATH_CURRENCY, CURRENCY);
        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CURRENCY:
                return CurrencyEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CurrencyDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CURRENCY:
                cursor = mOpenHelper.getReadableDatabase().query(
                        CurrencyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CURRENCY:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(CurrencyEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            count++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                Log.d(TAG, "Number of entries " + count);
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}