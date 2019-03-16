package com.example.time18_boershjaelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private EditText editTextAntalAktier;
    private EditText editTextKoebskurs;
    private EditText editTextKurtage;

    private TextView textViewBreakeven;
    private TextView textViewEn;
    private TextView textViewFem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAntalAktier = findViewById(R.id.editTextAA);
        editTextKoebskurs = findViewById(R.id.editTextKK);
        editTextKurtage = findViewById(R.id.editTextK);

        textViewBreakeven = findViewById(R.id.textViewBE);
        textViewEn = findViewById(R.id.textViewEn);
        textViewFem = findViewById(R.id.textViewFem);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double aa = Double.parseDouble(editTextAntalAktier.getText().toString());
                    double kk = Double.parseDouble(editTextKoebskurs.getText().toString());
                    double k = Double.parseDouble(editTextKurtage.getText().toString());

                    double be = Kurvekseler.breakeven(aa, kk, k);
                    double en = Kurvekseler.tjentEn(aa, kk, k);
                    double fem  = Kurvekseler.tjentFem(aa, kk, k);

                    String formatBE = String.format(Locale.ENGLISH, "%4.2f", be);
                    String formatEn = String.format(Locale.ENGLISH, "%4.2f", en);
                    String formatFem = String.format(Locale.ENGLISH, "%4.2f", fem);

                    textViewBreakeven.setText(formatBE);
                    textViewEn.setText(formatEn);
                    textViewFem.setText(formatFem);

//                    textViewBreakeven.setText(String.valueOf(be));
//                    textViewEn.setText(String.valueOf(en));
//                    textViewFem.setText(String.valueOf(fem));

                } catch (NumberFormatException e) {
                    System.out.println("Nope1");
                } catch (Exception e) {
                    System.out.println("Nope2");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        editTextAntalAktier.addTextChangedListener(watcher);
        editTextKoebskurs.addTextChangedListener(watcher);
        editTextKurtage.addTextChangedListener(watcher);

        setupSharedPreferences();
    }

    public void onClickShareTextButton(View v) {
        String beregning = "Breakeven ved kurs: " + textViewBreakeven.getText().toString() + "\n" +
                "Tjent 1% ved kurs: " + textViewEn.getText().toString() + "\n" +
                "Tjent 5% ved kurs: " + textViewFem.getText().toString();
        shareText(beregning);
    }

    private void shareText(String textToShare) {
        String mimeType = "text/plain";
        String title = "Kursberegner";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String aaString = sharedPreferences.getString(getString(R.string.pref_antalAktier_key), getString(R.string.pref_antalAktier_default));
//        float aaFloat = Float.parseFloat(aaString);
        editTextAntalAktier.setText(aaString);

        String kk = sharedPreferences.getString(getString(R.string.pref_koebskurs_key), getString(R.string.pref_koebskurs__default));
        editTextKoebskurs.setText(kk);

        String k = sharedPreferences.getString(getString(R.string.pref_kurtage_key), getString(R.string.pref_kurtage_default));
        editTextKurtage.setText(k);

//        float minSize = Float.parseFloat(sharedPreferences.getString(getString(R.string.pref_size_key),
//                getString(R.string.pref_size_default)));
//        mVisualizerView.setMinSizeScale(minSize);

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_antalAktier_key))) {
            String aaString = sharedPreferences.getString(key, getString(R.string.pref_antalAktier_default));
            editTextAntalAktier.setText(aaString);
        } else if (key.equals(getString(R.string.pref_koebskurs_key))) {
            String kk = sharedPreferences.getString(key, getString(R.string.pref_koebskurs__default));
            editTextKoebskurs.setText(kk);
        } else if (key.equals(getString(R.string.pref_kurtage_key))) {
            String k = sharedPreferences.getString(key, getString(R.string.pref_kurtage_default));
            editTextKurtage.setText(k);
        }
    }
}
