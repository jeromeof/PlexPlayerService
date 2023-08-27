package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Media {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(isAttribute = true)
    private String duration;

    @JacksonXmlProperty(isAttribute = true)
    private String bitrate;

    @JacksonXmlProperty(isAttribute = true)
    private String audioChannels;

    @JacksonXmlProperty(isAttribute = true)
    private String audioCodec;

    @JacksonXmlProperty(isAttribute = true)
    private String container;

    private Part Part;

    // getters, setters, and other methods...

    public String getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(String audioChannels) {
        this.audioChannels = audioChannels;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public com.pragmaticaudio.plexservice.controllers.entities.Part getPart() {
        return Part;
    }

    public void setPart(com.pragmaticaudio.plexservice.controllers.entities.Part part) {
        Part = part;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}