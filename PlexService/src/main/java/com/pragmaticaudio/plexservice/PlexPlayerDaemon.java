package com.pragmaticaudio.plexservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexServerInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class PlexPlayerDaemon extends Service {

    // First some settings which can be in the intent
    public static final String NAME = "NAME";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PLEX_PORT = "PORT";
    public static final String DEVICE_UUID = "UUID";

    // A message handler so we can toast messages from the service
    private Handler messageHandler = null;

    // The actual PlexPlayerServer implementing the required rest endpoints
    private PlexPlayerServer plexPlayerServer = null;

    private PlexGDM plexGDM = new PlexGDM();


    private HandlerThread discoveryThread;
    private Handler discoveryHandler;

    private HandlerThread broadcastThread;
    private Handler broadcastHandler;
    List<PlexServerInfo> plexServers = new LinkedList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    Map<String, Object> settings = getSettingsFromBundle(bundle);

                    // Use GDM to search for local plex servers
                    //searchForPlexServers();

                    // Finally broadcast our availability over GDM
                    declareOurAvailability(settings);

                    plexPlayerServer = new PlexPlayerServer(settings, new AndroidLogger());

                    plexPlayerServer.start();

                    // So we can toast messages up to the main app
                    messageHandler = new Handler(getApplicationContext().getMainLooper());
                }
            } catch (Exception e) {
                Log.e("PlexPlayer", "Exception on Startup " + e.getLocalizedMessage());
            }
        }
        return Service.START_STICKY;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // Wi-Fi Network
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    return (ipAddress & 0xFF) + "." +
                            ((ipAddress >> 8) & 0xFF) + "." +
                            ((ipAddress >> 16) & 0xFF) + "." +
                            ((ipAddress >> 24) & 0xFF);
                }
            }

            // Mobile Network
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface networkInterface : networkInterfaces) {
                        if (!networkInterface.isLoopback()) {
                            List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                            for (InetAddress inetAddress : inetAddresses) {
                                if (!inetAddress.isLoopbackAddress()) {
                                    return inetAddress.getHostAddress();
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private Map<String, Object> getSettingsFromBundle(Bundle bundle) {
        Map<String, Object> settings = new HashMap<>();

        //TODO: Put settings in the same format as the .plexPlayer.yml file

        settings.put( "", bundle.get("plex_port"));

        return settings;
    }


    private void declareOurAvailability(Map<String, Object> settings) {
        broadcastThread = new HandlerThread("GDMBroadcastThread");
        broadcastThread.start();

        PlexPlayerInfo playerInfo = createPlayerInfo(settings);

        broadcastHandler = new Handler(broadcastThread.getLooper());
        broadcastHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    plexGDM.declareAvailability(playerInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Do this again in 5 seconds
                broadcastHandler.postDelayed(this, 5000);
            }
        });
    }

    private PlexPlayerInfo createPlayerInfo(Map<String, Object> settings) {
        return new PlexPlayerInfo(settings);
    }

    // Create a thread to search for plex servers
    private void searchForPlexServers() {
        discoveryThread = new HandlerThread("GDMDiscoveryThread");
        discoveryThread.start();

        discoveryHandler = new Handler(discoveryThread.getLooper());
        discoveryHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("PlexGDM", "Search Broadcasted");

                    plexServers = plexGDM.searchForPlexServers(plexServers);

                    // Do this again in 5 seconds
                    discoveryHandler.postDelayed(this, 10000);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onCreate() {
        toast("Plex Player Service Created (1.0)");

    }

    /**
     * Toast a message to the UI - handy for diagnosing low level issues
     * Disable and remove messageHandler if we don't want it
     * @param message
     */
    public void toast(String message) {
        if (messageHandler == null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            // Although you need to pass an appropriate context
            messageHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stopping the player when service is destroyed

        toast("Plex Player Stopped");

        // Close our threads
        discoveryThread.quitSafely();
//        broadcastThread.quitSafely();

        // Stop the actual PlexPlayer from listening
        if (plexPlayerServer != null) {
            plexPlayerServer.stop();

            plexPlayerServer = null;
        }
    }

}
