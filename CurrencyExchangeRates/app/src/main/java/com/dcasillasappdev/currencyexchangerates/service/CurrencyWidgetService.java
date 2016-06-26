package com.dcasillasappdev.currencyexchangerates.service;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyContract;
import com.dcasillasappdev.currencyexchangerates.utils.Constants;
import com.dcasillasappdev.currencyexchangerates.utils.Utils;

public class CurrencyWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                String baseCode = Utils.getBaseCurrency(getApplicationContext());
                String date = Utils.getCurrentDate(Constants.DATE_FORMAT);
                data = getContentResolver().query(CurrencyContract.CurrencyEntry.CONTENT_URI,
                        Constants.CURRENCY_COLUMNS,
                        Constants.sBaseDateSelection,
                        new String[]{baseCode, date},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.list_item);
                String target = data.getString(Constants.COLUMN_TARGET);
                double rate = data.getDouble(Constants.COLUMN_RATE);
                views.setTextViewText(R.id.currencyName, target);
                views.setTextViewText(R.id.currencyRate, String.valueOf(rate));
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}