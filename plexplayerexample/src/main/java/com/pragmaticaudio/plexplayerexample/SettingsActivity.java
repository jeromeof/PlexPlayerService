package com.pragmaticaudio.plexplayerexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.pragmaticaudio.plexservice.PlexCloudService;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String PLEX_TOKEN = "plex_token";
    public static final String PLEXAMP_PORT = "plex_port";
    public static final String DEVICE_UUID = "device_uuid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if we have a pin_id in the shared pref
        // if we do then see can we immediately verify it

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        PlexCloudService plexCloudService = new PlexCloudService();

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if ("plex_linked".equals(key)) {
                // The key_pin preference has been changed
                boolean plex_linked = sharedPreferences.getBoolean(key,  false);
                // Do something with the new value if required
                if (plex_linked == false) {
                    // Lets generate a PIN and pass this pin to a new intent to allow user link their account
                    try {
                        PlexPinInfo pin = plexCloudService.getPin(MainActivity.getPlexPlayerInfo(sharedPreferences));

                        // Save the pin_id so we can verify it the next time the preferences screen is loaded
                        sharedPreferences.edit().putString("pin_id", pin.getId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        String pin_id = sharedPreferences.getString("pin_id", null);
                        if (pin_id != null) {
                            PlexPinInfo pin = plexCloudService.checkPin(pin_id, MainActivity.getPlexPlayerInfo(sharedPreferences));

                            // Save the pin_id so we can verify it the next time the preferences screen is loaded
                            sharedPreferences.edit().putString("token", pin.getAuthToken());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            // Similarly, you can check for other keys
        }
    }
}