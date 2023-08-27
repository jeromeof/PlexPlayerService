package com.pragmaticaudio.plexservice.controllers.entities;

public enum TypeEnum {
    MUSIC("music"),
    PHOTO("photo"),
    VIDEO("video");

    private final String value;

    TypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}