package com.pragmaticaudio.plexservice;

public class ConsoleLogger implements PlexPlayerLogger {
    @Override
    public void logMessage(String message) {
        System.out.println("LOG:" + message);
    }
}
