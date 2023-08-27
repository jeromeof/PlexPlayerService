package com.pragmaticaudio.plexservice.controllers.entities;

public enum StateEnum {
    PAUSED("paused"),
    PLAYING("playing"),
    STOPPED("stopped");

    private final String value;

    StateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
