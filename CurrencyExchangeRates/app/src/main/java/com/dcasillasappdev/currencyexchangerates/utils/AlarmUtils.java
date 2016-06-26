package com.dcasillasappdev.currencyexchangerates.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmUtils {

    private static AlarmManager sAlarmManager;
    private static PendingIntent sPendingIntent;

    private static final String TAG = AlarmUtils.class.getSimpleName();

    public static void startAlarmService(Context context, Intent intent) {
        stopAlarmService();
        sPendingIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        sAlarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), sPendingIntent);
    }

    public static void stopAlarmService() {
        if(sPendingIntent != null && sAlarmManager != null) {
            sAlarmManager.cancel(sPendingIntent);
        }
    }
}