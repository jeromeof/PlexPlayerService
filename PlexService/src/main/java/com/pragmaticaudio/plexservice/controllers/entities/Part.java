package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Part {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(isAttribute = true)
    private String key;

    @JacksonXmlProperty(isAttribute = true)
    private String duration;

    @JacksonXmlProperty(isAttribute = true)
    private String file;

    @JacksonXmlProperty(isAttribute = true)
    private String size;

    @JacksonXmlProperty(isAttribute = true)
    private String container;

    @JacksonXmlProperty(isAttribute = true)
    private String hasThumbnail;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlProperty(localName = "Stream")
    private Stream stream;

    // getters, setters, and other methods...

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public com.pragmaticaudio.plexservice.controllers.entities.Stream getStream() {
        return this.stream;
    }

    public void setStream(com.pragmaticaudio.plexservice.controllers.entities.Stream stream) {
        this.stream = stream;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHasThumbnail() {
        return hasThumbnail;
    }

    public void setHasThumbnail(String hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
    }
}