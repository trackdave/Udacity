package com.dcasillasappdev.currencyexchangerates.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.dcasillasappdev.currencyexchangerates.R;
import com.dcasillasappdev.currencyexchangerates.data.CurrencyDBHelper;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference clear = getPreferenceManager().findPreference(getString(R.string.pref_clear));
        clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_delete_sweep_white_48dp);
                builder.setTitle(R.string.pref_clear_title);
                builder.setMessage(R.string.message_clear_all);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CurrencyDBHelper db = new CurrencyDBHelper(getActivity());
                        db.clearTable();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.create().show();
                return true;
            }
        });
    }
}