package com.pragmaticaudio.plexservice;

import android.util.Log;

public class AndroidLogger implements PlexPlayerLogger {
    @Override
    public void logMessage(String message) {
        Log.e("PlexPlayer", message);
    }
}
