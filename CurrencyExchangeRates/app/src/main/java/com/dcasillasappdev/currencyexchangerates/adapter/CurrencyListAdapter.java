package com.dcasillasappdev.currencyexchangerates.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.model.Currency;

import java.util.List;

public class CurrencyListAdapter extends ArrayAdapter<Currency> {

    private final LayoutInflater inflater;

    private static class ViewHolder {
        TextView target;
        TextView rate;
        public ViewHolder(View view) {
            target = (TextView) view.findViewById(R.id.currencyName);
            rate = (TextView) view.findViewById(R.id.currencyRate);
        }
    }

    public CurrencyListAdapter(Context context) {
        super(context, R.layout.list_item);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCurrencyList(List<Currency> currencies) {
        clear();
        if (currencies != null) {
            addAll(currencies);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Currency currency = getItem(position);
        holder.target.setText(currency.getTarget());
        double rate = currency.getRate();
        if (rate == 0) {
            holder.rate.setText("");
        } else {
            holder.rate.setText(String.valueOf(rate));
        }

        return view;
    }
}
