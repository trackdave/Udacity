package com.dcasillasappdev.currencyexchangerates.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dcasillasappdev.currencyexchangerates.utils.Constants;
import com.dcasillasappdev.currencyexchangerates.utils.NotificationUtils;

public class CurrencyReceiver extends BroadcastReceiver {

    private static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle args = intent.getExtras();
        if (args.containsKey(Constants.MESSAGE)) {
            if (!NotificationUtils.isAppInBackground(context)) {
                Toast.makeText(context, args.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
            }
        }
    }
}