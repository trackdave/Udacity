package com.dcasillasappdev.currencyexchangerates.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract.CurrencyEntry;

public class CurrencyDBHelper extends SQLiteOpenHelper {

    private static final String TAG = CurrencyDBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "currency.db";

    private static final String CREATE_TABLE_CURRENCY = "CREATE TABLE "
            + CurrencyEntry.TABLE_NAME + " ("
            + CurrencyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CurrencyEntry.COLUMN_BASE + " TEXT,"
            + CurrencyEntry.COLUMN_TARGET + " TEXT,"
            + CurrencyEntry.COLUMN_RATE + " REAL,"
            + CurrencyEntry.COLUMN_DATE + " TEXT)";

    public CurrencyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_CURRENCY);
            Log.d(TAG, "Successful creating table currency");
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d(TAG, "Error creating table currency");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CurrencyEntry.TABLE_NAME, null, null);
        db.close();
    }

    public boolean hasBaseAndDate(String base, String date) {
        boolean hasBaseAndDate;
        SQLiteDatabase db = getReadableDatabase();
        String selection = CurrencyEntry.COLUMN_BASE + "=? and " + CurrencyEntry.COLUMN_DATE + "=?";
        String[] selectionArgs = {base, date};
        Cursor cursor = db.query(CurrencyEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            hasBaseAndDate = true;
            Log.d(TAG, "Database has data for " + base + " on " + date);
        } else {
            hasBaseAndDate = false;
            Log.d(TAG, "Database has no data for " + base + " on " + date);
        }
        cursor.close();
        db.close();
        return hasBaseAndDate;
    }
}