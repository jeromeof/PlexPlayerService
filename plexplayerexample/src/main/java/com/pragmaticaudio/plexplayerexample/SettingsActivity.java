package com.pragmaticaudio.plexplayerexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.pragmaticaudio.plexservice.PlexCloudService;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String PLEXAMP_PORT = "plex_port";
    public static final String DEVICE_UUID = "device_uuid";

    public static final String PRODUCT_NAME = "product_name";

    public static final String NAME = "name";


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

        private Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onResume() {
            super.onResume();

            // Register the listener
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            super.onPause();
            // Unregister the listener
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if ("plex_linked".equals(key)) {
                // The key_pin preference has been changed
                boolean plex_linked = sharedPreferences.getBoolean(key,  false);
                // Do something with the new value if required
                if (plex_linked) { // If radio is turned on - lets get a Pin and wait to verify it
                    // Lets generate a PIN and pass this pin to a new intent to allow user link their account
                    PlexPlayerInfo plexPlayerInfo = MainActivity.getPlexPlayerInfo(sharedPreferences);

                    // Start a new thread to make the backend call
                    new Thread(new VerifyPinThread(plexPlayerInfo)).start();
                }
            }
            // Similarly, you can check for other keys
        }


        private void openWebBrowser(String url) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Ensure there's an app to handle the intent
            if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(browserIntent);
            } else {
                Toast.makeText(getActivity(), "No browser found to open the link", Toast.LENGTH_SHORT).show();
            }
        }
        private class VerifyPinThread implements Runnable {
            private PlexPinInfo pin;
            private final PlexPlayerInfo plexPlayerInfo;

            public VerifyPinThread(PlexPlayerInfo plexPlayerInfo) {
                this.plexPlayerInfo = plexPlayerInfo;
            }

            @Override
            public void run() {
                try {
                    pin = plexCloudService.getPin(plexPlayerInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // How launch a webbrowser with the correct URL to allow
                // user to login and verify the PIN code
                openWebBrowser("https://www.plex.tv/link/?pin=" + pin.getCode());

                String id = pin.getId();
                for (int i = 0; i < 60; i++) {  // Give it 60 seconds to allow user to login and verify
                    // Simulate checking a value from the backend
                    try {
                        pin = plexCloudService.checkPin(id, plexPlayerInfo);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String token = pin.getAuthToken();
                    if (token != null && token.length() > 0) {
                        SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                        editor.putString(PlexPlayerInfo.PLEX_TOKEN, pin.getAuthToken());
                        editor.apply();
                        break;  // Break out of the loop if we found the desired value
                    }

                    try {
                        Thread.sleep(1000);  // wait for a second before checking again
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}