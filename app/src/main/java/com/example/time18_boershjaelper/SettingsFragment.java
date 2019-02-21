package com.example.time18_boershjaelper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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

        // Go through all of the preferences, and set up their preference summary.
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
                System.out.println(p.getKey());
                System.out.println(value);
            }
        }

        // COMPLETED (3) Add the OnPreferenceChangeListener specifically to the EditTextPreference
        // Add the preference listener which checks that the size is correct to the size preference
        Preference preference1 = findPreference(getString(R.string.pref_antalAktier_key));
        preference1.setOnPreferenceChangeListener(this);

        Preference preference2 = findPreference(getString(R.string.pref_koebskurs_key));
        preference2.setOnPreferenceChangeListener(this);

        Preference preference3 = findPreference(getString(R.string.pref_kurtage_key));
        preference3.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference preference = findPreference(key);
        if (null != preference) {
            // Updates the summary for the preference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }

    // COMPLETED (2) Override onPreferenceChange. This method should try to convert the new preference value
    // to a float; if it cannot, show a helpful error message and return false. If it can be converted
    // to a float check that that float is between 0 (exclusive) and 3 (inclusive). If it isn't, show
    // an error message and return false. If it is a valid number, return true.

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // In this context, we're using the onPreferenceChange listener for checking whether the
        // size setting was set to a valid value.

        Toast error = Toast.makeText(getContext(), "Please select a number between 0.1 and 3", Toast.LENGTH_SHORT);

        Toast error1 = Toast.makeText(getContext(), "Indtast heltal", Toast.LENGTH_SHORT);
        Toast error2 = Toast.makeText(getContext(), "Indtast hel- eller kommatal", Toast.LENGTH_SHORT);

        // Double check that the preference is the size preference
        String antalAktierKey = getString(R.string.pref_antalAktier_key);
        String koebskursKey = getString(R.string.pref_koebskurs_key);
        String kurtageKey = getString(R.string.pref_kurtage_key);

        String stringSize = (String) newValue;

        if (preference.getKey().equals(antalAktierKey)) {
            try {
                float size = Float.parseFloat(stringSize);
                // If the number is outside of the acceptable range, show an error.
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