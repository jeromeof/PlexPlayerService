package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Track {

    @JacksonXmlProperty(isAttribute = true)
    private String playQueueItemID;

    @JacksonXmlProperty(isAttribute = true)
    private String ratingKey;

    @JacksonXmlProperty(isAttribute = true)
    private String key;

    @JacksonXmlProperty(isAttribute = true)
    private String parentRatingKey;

    @JacksonXmlProperty(isAttribute = true)
    private String grandparentRatingKey;

    @JacksonXmlProperty(isAttribute = true)
    private String guid;

    @JacksonXmlProperty(isAttribute = true)
    private String parentGuid;

    @JacksonXmlProperty(isAttribute = true)
    private String grandparentGuid;

    @JacksonXmlProperty(isAttribute = true)
    private String parentStudio;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String title;

    @JacksonXmlProperty(isAttribute = true)
    private String grandparentKey;

    @JacksonXmlProperty(isAttribute = true)
    private String parentKey;

    @JacksonXmlProperty(isAttribute = true)
    private String librarySectionTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String librarySectionID;

    @JacksonXmlProperty(isAttribute = true)
    private String librarySectionKey;

    @JacksonXmlProperty(isAttribute = true)
    private String grandparentTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String parentTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String summary;

    @JacksonXmlProperty(isAttribute = true)
    private String index;

    @JacksonXmlProperty(isAttribute = true)
    private String parentIndex;

    @JacksonXmlProperty(isAttribute = true)
    private String ratingCount;

    @JacksonXmlProperty(isAttribute = true)
    private String viewCount;

    @JacksonXmlProperty(isAttribute = true)
    private String skipCount;

    @JacksonXmlProperty(isAttribute = true)
    private String lastViewedAt;

    @JacksonXmlProperty(isAttribute = true)
    private String parentYear;

    @JacksonXmlProperty(isAttribute = true)
    private String thumb;

    @JacksonXmlProperty(isAttribute = true)
    private String parentThumb;

    @JacksonXmlProperty(isAttribute = true)
    private String grandparentThumb;

    @JacksonXmlProperty(isAttribute = true)
    private String duration;

    @JacksonXmlProperty(isAttribute = true)
    private String addedAt;

    @JacksonXmlProperty(isAttribute = true)
    private String updatedAt;

    @JacksonXmlProperty(isAttribute = true)
    private String musicAnalysisVersion;

    @JacksonXmlProperty(isAttribute = true)
    private String source;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlProperty(localName = "Media")
    private Media media;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlProperty(localName = "Guid")
    private Guid Guid;

    @JacksonXmlProperty(isAttribute = true)
    private String titleSort;
    private String art;

    private String grandparentArt;

    // getters, setters, and other methods...

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public com.pragmaticaudio.plexservice.controllers.entities.Media getMedia() {
        return this.media;
    }

    public void setMedia(com.pragmaticaudio.plexservice.controllers.entities.Media media) {
        this.media = media;
    }

    public String getPlayQueueItemID() {
        return playQueueItemID;
    }

    public void setPlayQueueItemID(String playQueueItemID) {
        this.playQueueItemID = playQueueItemID;
    }

    public String getRatingKey() {
        return ratingKey;
    }

    public void setRatingKey(String ratingKey) {
        this.ratingKey = ratingKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParentRatingKey() {
        return parentRatingKey;
    }

    public void setParentRatingKey(String parentRatingKey) {
        this.parentRatingKey = parentRatingKey;
    }

    public String getGrandparentRatingKey() {
        return grandparentRatingKey;
    }

    public void setGrandparentRatingKey(String grandparentRatingKey) {
        this.grandparentRatingKey = grandparentRatingKey;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public String getGrandparentGuid() {
        return grandparentGuid;
    }

    public void setGrandparentGuid(String grandparentGuid) {
        this.grandparentGuid = grandparentGuid;
    }

    public String getParentStudio() {
        return parentStudio;
    }

    public void setParentStudio(String parentStudio) {
        this.parentStudio = parentStudio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrandparentKey() {
        return grandparentKey;
    }

    public void setGrandparentKey(String grandparentKey) {
        this.grandparentKey = grandparentKey;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getLibrarySectionTitle() {
        return librarySectionTitle;
    }

    public void setLibrarySectionTitle(String librarySectionTitle) {
        this.librarySectionTitle = librarySectionTitle;
    }

    public String getLibrarySectionID() {
        return librarySectionID;
    }

    public void setLibrarySectionID(String librarySectionID) {
        this.librarySectionID = librarySectionID;
    }

    public String getLibrarySectionKey() {
        return librarySectionKey;
    }

    public void setLibrarySectionKey(String librarySectionKey) {
        this.librarySectionKey = librarySectionKey;
    }

    public String getGrandparentTitle() {
        return grandparentTitle;
    }

    public void setGrandparentTitle(String grandparentTitle) {
        this.grandparentTitle = grandparentTitle;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(String parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(String skipCount) {
        this.skipCount = skipCount;
    }

    public String getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(String lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public String getParentYear() {
        return parentYear;
    }

    public void setParentYear(String parentYear) {
        this.parentYear = parentYear;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getParentThumb() {
        return parentThumb;
    }

    public void setParentThumb(String parentThumb) {
        this.parentThumb = parentThumb;
    }

    public String getGrandparentThumb() {
        return grandparentThumb;
    }

    public void setGrandparentThumb(String grandparentThumb) {
        this.grandparentThumb = grandparentThumb;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMusicAnalysisVersion() {
        return musicAnalysisVersion;
    }

    public void setMusicAnalysisVersion(String musicAnalysisVersion) {
        this.musicAnalysisVersion = musicAnalysisVersion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getTitleSort() {
        return titleSort;
    }

    public void setTitleSort(String titleSort) {
        this.titleSort = titleSort;
    }

    public String getGrandparentArt() {
        return grandparentArt;
    }

    public void setGrandparentArt(String grandparentArt) {
        this.grandparentArt = grandparentArt;
    }

    public void setGuid(com.pragmaticaudio.plexservice.controllers.entities.Guid guid) {
        Guid = guid;
    }
}