package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "MediaContainer")
public class PlayQueueMediaContainer {

    @JacksonXmlProperty(isAttribute = true)
    private int size;

    @JacksonXmlProperty(isAttribute = true)
    private String identifier;

    @JacksonXmlProperty(isAttribute = true)
    private String mediaTagPrefix;

    @JacksonXmlProperty(isAttribute = true)
    private long mediaTagVersion;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueID;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueLastAddedItemID;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueSelectedItemID;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueSelectedItemOffset;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueSelectedMetadataItemID;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueShuffled;

    @JacksonXmlProperty(isAttribute = true)
    private String playQueueSourceURI;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueTotalCount;

    @JacksonXmlProperty(isAttribute = true)
    private int playQueueVersion;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Track> Track = null;

    // Getters, setters, and other necessary methods


    public List<com.pragmaticaudio.plexservice.controllers.entities.Track> getTrack() {
        return Track;
    }

    public void setTrack(List<com.pragmaticaudio.plexservice.controllers.entities.Track> track) {
        Track = track;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMediaTagPrefix() {
        return mediaTagPrefix;
    }

    public void setMediaTagPrefix(String mediaTagPrefix) {
        this.mediaTagPrefix = mediaTagPrefix;
    }

    public long getMediaTagVersion() {
        return mediaTagVersion;
    }

    public void setMediaTagVersion(long mediaTagVersion) {
        this.mediaTagVersion = mediaTagVersion;
    }

    public int getPlayQueueID() {
        return playQueueID;
    }

    public void setPlayQueueID(int playQueueID) {
        this.playQueueID = playQueueID;
    }

    public int getPlayQueueLastAddedItemID() {
        return playQueueLastAddedItemID;
    }

    public void setPlayQueueLastAddedItemID(int playQueueLastAddedItemID) {
        this.playQueueLastAddedItemID = playQueueLastAddedItemID;
    }

    public int getPlayQueueSelectedItemID() {
        return playQueueSelectedItemID;
    }

    public void setPlayQueueSelectedItemID(int playQueueSelectedItemID) {
        this.playQueueSelectedItemID = playQueueSelectedItemID;
    }

    public int getPlayQueueSelectedItemOffset() {
        return playQueueSelectedItemOffset;
    }

    public void setPlayQueueSelectedItemOffset(int playQueueSelectedItemOffset) {
        this.playQueueSelectedItemOffset = playQueueSelectedItemOffset;
    }

    public int getPlayQueueSelectedMetadataItemID() {
        return playQueueSelectedMetadataItemID;
    }

    public void setPlayQueueSelectedMetadataItemID(int playQueueSelectedMetadataItemID) {
        this.playQueueSelectedMetadataItemID = playQueueSelectedMetadataItemID;
    }

    public int getPlayQueueShuffled() {
        return playQueueShuffled;
    }

    public void setPlayQueueShuffled(int playQueueShuffled) {
        this.playQueueShuffled = playQueueShuffled;
    }

    public String getPlayQueueSourceURI() {
        return playQueueSourceURI;
    }

    public void setPlayQueueSourceURI(String playQueueSourceURI) {
        this.playQueueSourceURI = playQueueSourceURI;
    }

    public int getPlayQueueTotalCount() {
        return playQueueTotalCount;
    }

    public void setPlayQueueTotalCount(int playQueueTotalCount) {
        this.playQueueTotalCount = playQueueTotalCount;
    }

    public int getPlayQueueVersion() {
        return playQueueVersion;
    }

    public void setPlayQueueVersion(int playQueueVersion) {
        this.playQueueVersion = playQueueVersion;
    }
}

