package com.dcasillasappdev.currencyexchangerates.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.utils.Constants;

public class CurrencyCursorAdapter extends CursorAdapter {

    private static class ViewHolder {
        TextView target;
        TextView rate;

        public ViewHolder(View view) {
            target = (TextView) view.findViewById(R.id.currencyName);
            rate = (TextView) view.findViewById(R.id.currencyRate);
        }
    }

    public CurrencyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String target = cursor.getString(Constants.COLUMN_TARGET);
        viewHolder.target.setText(target);

        double rate = cursor.getDouble(Constants.COLUMN_RATE);
        if (rate == 0) {
            viewHolder.rate.setText("");
        } else {
            viewHolder.rate.setText(String.valueOf(rate));
        }
    }
}