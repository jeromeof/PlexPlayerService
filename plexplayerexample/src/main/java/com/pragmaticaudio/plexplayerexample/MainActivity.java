package com.pragmaticaudio.plexplayerexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.os.Build;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        startPlexService();

        Log.e("PlexPlayer", Utils.getIPAddress(true));
    }

    /**
     * Example of starting plex server -
     */
    private void startPlexService() {

        // Gather some data from the app + device to use in our Plex instance
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // Make sure the sharedPref have the UUID for this instance of the App
        sharedPref.edit().putString(SettingsActivity.DEVICE_UUID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        PlexPlayerInfo plexPlayerInfo = getPlexPlayerInfo(sharedPref);

        //  Create the PlexPlayerDaemon intent to pass this data to the service
        Intent intent = new Intent(this, PlexPlayerDaemon.class);
        intent.putExtra(SettingsActivity.PLEX_TOKEN, sharedPref.getString(SettingsActivity.PLEX_TOKEN, "0"));
        intent.putExtra(PlexPlayerDaemon.NAME, plexPlayerInfo.getName());
        intent.putExtra(PlexPlayerDaemon.PRODUCT_NAME, plexPlayerInfo.getProduct());
        intent.putExtra(PlexPlayerDaemon.PLEX_PORT, plexPlayerInfo.getPort());
        intent.putExtra(PlexPlayerDaemon.DEVICE_UUID, plexPlayerInfo.getResourceIdentifier());

        // Actually start the service
        startService(intent);

    }

    static protected PlexPlayerInfo getPlexPlayerInfo(SharedPreferences sharedPref) {
        String deviceUUID = sharedPref.getString(SettingsActivity.DEVICE_UUID, "");
        String deviceName = Build.MODEL;
        String productName = Build.DEVICE;
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