package com.pragmaticaudio.plexplayerexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pragmaticaudio.plexplayerexample.databinding.ActivityMainBinding;
import com.pragmaticaudio.plexservice.PlexPlayerDaemon;
import com.pragmaticaudio.plexservice.PlexPlayerServer;
import com.pragmaticaudio.plexservice.Utils;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private PlexPlayerDaemon plexPlayerDaemon;
    private boolean isPlexPlayerBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        startPlexService();

        Log.e("PlexPlayer", Utils.getIPAddress(true));
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlexPlayerDaemon.LocalBinder binder = (PlexPlayerDaemon.LocalBinder) service;
            plexPlayerDaemon = binder.getService();
            isPlexPlayerBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isPlexPlayerBound = false;
        }
    };
    /**
     * Example of starting plex server -
     */
    private void startPlexService() {

        // Gather some data from the app + device to use in our Plex instance
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPrefs, key) -> {
            // Check if the key you're interested in has changed
            if (PlexPlayerInfo.PLEX_TOKEN.equals(key)) {
                // Reload MainActivity or refresh the data/content.
                recreate();
            }
        };

        // Check for the Token - no token - lets move user to the Settings to get a token
        String token = sharedPref.getString(PlexPlayerInfo.PLEX_TOKEN, "0");
        if (token == null || token.length() <= 1) { // Token needs to be a valid value

            // Setup some default into the sharedPref - so they become the defaults
            SharedPreferences.Editor prefEdit = sharedPref.edit();
            prefEdit.putString(SettingsActivity.DEVICE_UUID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            prefEdit.putString(PlexPlayerDaemon.NAME, "Standalone");
            prefEdit.putString(PlexPlayerDaemon.PRODUCT_NAME, "PlexPlayer");
            prefEdit.commit();

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            finish();  // Finish this activity - will restart once the settings are entered
            return;
        }

        PlexPlayerInfo plexPlayerInfo = getPlexPlayerInfo(sharedPref);

        //  Create the PlexPlayerDaemon intent to pass this data to the service
        Intent intent = new Intent(this, PlexPlayerDaemon.class);
        intent.putExtra(PlexPlayerInfo.PLEX_TOKEN, token);
        intent.putExtra(PlexPlayerDaemon.NAME, plexPlayerInfo.getName());
        intent.putExtra(PlexPlayerDaemon.PRODUCT_NAME, plexPlayerInfo.getProduct());
        intent.putExtra(PlexPlayerDaemon.PLEX_PORT, plexPlayerInfo.getPort());
        intent.putExtra(PlexPlayerDaemon.DEVICE_UUID, plexPlayerInfo.getResourceIdentifier());

        // Actually start the service
        startService(intent);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlexPlayerBound) {
            unbindService(serviceConnection);
            isPlexPlayerBound = false;
        }
    }


    static protected PlexPlayerInfo getPlexPlayerInfo(SharedPreferences sharedPref) {
        String deviceUUID = sharedPref.getString(SettingsActivity.DEVICE_UUID, "");
        String deviceName = sharedPref.getString(SettingsActivity.NAME, "PlexPlayer");
        String productName = sharedPref.getString(SettingsActivity.PRODUCT_NAME, "Standalone");
        int port = Integer.valueOf(sharedPref.getString(SettingsActivity.PLEXAMP_PORT, ""+PlexPlayerServer.DEFAULT_PORT));

        return new PlexPlayerInfo(deviceName, productName, port, deviceUUID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}