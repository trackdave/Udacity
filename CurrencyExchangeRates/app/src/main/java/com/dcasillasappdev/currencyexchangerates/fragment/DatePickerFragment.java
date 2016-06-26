package com.dcasillasappdev.currencyexchangerates.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    public interface DatePickerListener {
        void getRates(Calendar calendar);
    }

    private DatePickerListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (DatePickerListener) activity;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        listener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        Calendar minCal = new GregorianCalendar(2000, Calendar.JANUARY, 1);
        datePicker.setMinDate(minCal.getTimeInMillis());
        Calendar maxCal = new GregorianCalendar(year, month, day);
        datePicker.setMaxDate(maxCal.getTimeInMillis());

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(year, month, day);
        listener.getRates(calendar);
    }
}