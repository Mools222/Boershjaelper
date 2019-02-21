package com.example.time18_boershjaelper;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
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

    private EditText ETantalAktier;
    private EditText ETkoebskurs;
    private EditText ETkurtage;

    private TextView TWbreakeven;
    private TextView TWen;
    private TextView TWfem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ETantalAktier = findViewById(R.id.editTextAA);
        ETkoebskurs = findViewById(R.id.editTextKK);
        ETkurtage = findViewById(R.id.editTextK);

        TWbreakeven = findViewById(R.id.textViewBE);
        TWen = findViewById(R.id.textViewEn);
        TWfem = findViewById(R.id.textViewFem);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double aa = Double.parseDouble(ETantalAktier.getText().toString());
                    double kk = Double.parseDouble(ETkoebskurs.getText().toString());
                    double k = Double.parseDouble(ETkurtage.getText().toString());

                    double be = Kurvekseler.breakeven(aa, kk, k);
                    double en = Kurvekseler.tjentEn(aa, kk, k);
                    double fem  = Kurvekseler.tjentFem(aa, kk, k);

                    String formatBE = String.format(Locale.ENGLISH, "%4.2f", be);
                    String formatEn = String.format(Locale.ENGLISH, "%4.2f", en);
                    String formatFem = String.format(Locale.ENGLISH, "%4.2f", fem);

                    TWbreakeven.setText(formatBE);
                    TWen.setText(formatEn);
                    TWfem.setText(formatFem);

//                    TWbreakeven.setText(String.valueOf(be));
//                    TWen.setText(String.valueOf(en));
//                    TWfem.setText(String.valueOf(fem));

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

        ETantalAktier.addTextChangedListener(watcher);
        ETkoebskurs.addTextChangedListener(watcher);
        ETkurtage.addTextChangedListener(watcher);

        setupSharedPreferences();
    }

    public void onClickShareTextButton(View v) {
        String beregning = "Breakeven ved kurs: " + TWbreakeven.getText().toString() + "\n" +
                "Tjent 1% ved kurs: " + TWen.getText().toString() + "\n" +
                "Tjent 5% ved kurs: " + TWfem.getText().toString();
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
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
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
        ETantalAktier.setText(aaString);

        String kk = sharedPreferences.getString(getString(R.string.pref_koebskurs_key), getString(R.string.pref_koebskurs__default));
        ETkoebskurs.setText(kk);

        String k = sharedPreferences.getString(getString(R.string.pref_kurtage_key), getString(R.string.pref_kurtage_default));
        ETkurtage.setText(k);

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
            ETantalAktier.setText(aaString);
        } else if (key.equals(getString(R.string.pref_koebskurs_key))) {
            String kk = sharedPreferences.getString(key, getString(R.string.pref_koebskurs__default));
            ETkoebskurs.setText(kk);
        } else if (key.equals(getString(R.string.pref_kurtage_key))) {
            String k = sharedPreferences.getString(key, getString(R.string.pref_kurtage_default));
            ETkurtage.setText(k);
        }
    }


}
