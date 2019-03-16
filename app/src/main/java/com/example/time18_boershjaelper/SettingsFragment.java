package com.example.time18_boershjaelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_boers);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);

            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
                System.out.println(p.getKey());
                System.out.println(value);
            }
        }

        Preference preference1 = findPreference(getString(R.string.pref_antalAktier_key));
        preference1.setOnPreferenceChangeListener(this);

        Preference preference2 = findPreference(getString(R.string.pref_koebskurs_key));
        preference2.setOnPreferenceChangeListener(this);

        Preference preference3 = findPreference(getString(R.string.pref_kurtage_key));
        preference3.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);

            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Toast error1 = Toast.makeText(getContext(), "Indtast heltal", Toast.LENGTH_SHORT);
        Toast error2 = Toast.makeText(getContext(), "Indtast hel- eller kommatal", Toast.LENGTH_SHORT);

        String antalAktierKey = getString(R.string.pref_antalAktier_key);
        String koebskursKey = getString(R.string.pref_koebskurs_key);
        String kurtageKey = getString(R.string.pref_kurtage_key);

        String stringSize = (String) newValue;

        if (preference.getKey().equals(antalAktierKey)) {
            try {
                float size = Float.parseFloat(stringSize);
                if (size % 1 != 0) {
                    error1.show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                // If whatever the user entered can't be parsed to a number, show an error
                error1.show();
                return false;
            }
        } else if (preference.getKey().equals(koebskursKey) || preference.getKey().equals(kurtageKey)) {
            try {
                float size = Float.parseFloat(stringSize);
            } catch (NumberFormatException nfe) {
                // If whatever the user entered can't be parsed to a number, show an error
                error2.show();
                return false;
            }
        }

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}