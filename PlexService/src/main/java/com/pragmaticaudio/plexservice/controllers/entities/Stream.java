package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Stream {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(isAttribute = true)
    private String streamType;

    @JacksonXmlProperty(isAttribute = true)
    private String selected;

    @JacksonXmlProperty(isAttribute = true)
    private String codec;

    @JacksonXmlProperty(isAttribute = true)
    private String index;

    @JacksonXmlProperty(isAttribute = true)
    private String channels;

    @JacksonXmlProperty(isAttribute = true)
    private String bitrate;

    @JacksonXmlProperty(isAttribute = true)
    private String albumGain;

    @JacksonXmlProperty(isAttribute = true)
    private String albumPeak;

    @JacksonXmlProperty(isAttribute = true)
    private String albumRange;

    @JacksonXmlProperty(isAttribute = true)
    private String audioChannelLayout;

    @JacksonXmlProperty(isAttribute = true)
    private String bitDepth;

    @JacksonXmlProperty(isAttribute = true)
    private String endRamp;

    @JacksonXmlProperty(isAttribute = true)
    private String gain;

    @JacksonXmlProperty(isAttribute = true)
    private String loudness;

    @JacksonXmlProperty(isAttribute = true)
    private String lra;

    @JacksonXmlProperty(isAttribute = true)
    private String peak;

    @JacksonXmlProperty(isAttribute = true)
    private String samplingRate;

    @JacksonXmlProperty(isAttribute = true)
    private String startRamp;

    @JacksonXmlProperty(isAttribute = true)
    private String displayTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String extendedDisplayTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String key;

    @JacksonXmlProperty(isAttribute = true)
    private String format;

    @JacksonXmlProperty(isAttribute = true)
    private String minLines;

    @JacksonXmlProperty(isAttribute = true)
    private String provider;

    @JacksonXmlProperty(isAttribute = true)
    private String timed;

    // getters, setters, and other methods...

    public String getAlbumGain() {
        return albumGain;
    }

    public void setAlbumGain(String albumGain) {
        this.albumGain = albumGain;
    }

    public String getAlbumPeak() {
        return albumPeak;
    }

    public void setAlbumPeak(String albumPeak) {
        this.albumPeak = albumPeak;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreamType() {
        return streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getAlbumRange() {
        return albumRange;
    }

    public void setAlbumRange(String albumRange) {
        this.albumRange = albumRange;
    }

    public String getAudioChannelLayout() {
        return audioChannelLayout;
    }

    public void setAudioChannelLayout(String audioChannelLayout) {
        this.audioChannelLayout = audioChannelLayout;
    }

    public String getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(String bitDepth) {
        this.bitDepth = bitDepth;
    }

    public String getEndRamp() {
        return endRamp;
    }

    public void setEndRamp(String endRamp) {
        this.endRamp = endRamp;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }

    public String getLoudness() {
        return loudness;
    }

    public void setLoudness(String loudness) {
        this.loudness = loudness;
    }

    public String getLra() {
        return lra;
    }

    public void setLra(String lra) {
        this.lra = lra;
    }

    public String getPeak() {
        return peak;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    public String getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(String samplingRate) {
        this.samplingRate = samplingRate;
    }

    public String getStartRamp() {
        return startRamp;
    }

    public void setStartRamp(String startRamp) {
        this.startRamp = startRamp;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getExtendedDisplayTitle() {
        return extendedDisplayTitle;
    }

    public void setExtendedDisplayTitle(String extendedDisplayTitle) {
        this.extendedDisplayTitle = extendedDisplayTitle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMinLines() {
        return minLines;
    }

    public void setMinLines(String minLines) {
        this.minLines = minLines;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTimed() {
        return timed;
    }

    public void setTimed(String timed) {
        this.timed = timed;
    }
}