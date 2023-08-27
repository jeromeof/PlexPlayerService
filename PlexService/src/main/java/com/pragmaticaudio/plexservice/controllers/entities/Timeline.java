package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Timeline {

    @JacksonXmlProperty(isAttribute = true)
    private String state;

    @JacksonXmlProperty(isAttribute = true)
    private String duration;

    @JacksonXmlProperty(isAttribute = true)
    private String time;

    @JacksonXmlProperty(isAttribute = true)
    private String playQueueItemID;

    @JacksonXmlProperty(isAttribute = true)
    private String key;

    @JacksonXmlProperty(isAttribute = true)
    private String ratingKey;

    @JacksonXmlProperty(isAttribute = true)
    private String playQueueID;

    @JacksonXmlProperty(isAttribute = true)
    private String playQueueVersion;

    @JacksonXmlProperty(isAttribute = true)
    private String containerKey;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String itemType;

    @JacksonXmlProperty(isAttribute = true)
    private String volume;

    @JacksonXmlProperty(isAttribute = true)
    private String shuffle;

    @JacksonXmlProperty(isAttribute = true)
    private String repeat;

    @JacksonXmlProperty(isAttribute = true)
    private String controllable;

    @JacksonXmlProperty(isAttribute = true)
    private String machineIdentifier;

    @JacksonXmlProperty(isAttribute = true)
    private String protocol;

    @JacksonXmlProperty(isAttribute = true)
    private String address;

    @JacksonXmlProperty(isAttribute = true)
    private String port;

    private Track Track;

    // getters, setters, and other methods...

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContainerKey() {
        return containerKey;
    }

    public void setContainerKey(String containerKey) {
        this.containerKey = containerKey;
    }

    public Track getTrack() {
        return Track;
    }

    public void setTrack(Track track) {
        Track = track;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlayQueueItemID() {
        return playQueueItemID;
    }

    public void setPlayQueueItemID(String playQueueItemID) {
        this.playQueueItemID = playQueueItemID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRatingKey() {
        return ratingKey;
    }

    public void setRatingKey(String ratingKey) {
        this.ratingKey = ratingKey;
    }

    public String getPlayQueueID() {
        return playQueueID;
    }

    public void setPlayQueueID(String playQueueID) {
        this.playQueueID = playQueueID;
    }

    public String getPlayQueueVersion() {
        return playQueueVersion;
    }

    public void setPlayQueueVersion(String playQueueVersion) {
        this.playQueueVersion = playQueueVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getShuffle() {
        return shuffle;
    }

    public void setShuffle(String shuffle) {
        this.shuffle = shuffle;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getControllable() {
        return controllable;
    }

    public void setControllable(String controllable) {
        this.controllable = controllable;
    }

    public String getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}