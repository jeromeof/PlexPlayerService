package com.pragmaticaudio.plexservice.controllers.entities;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class MediaContainer {

    @JacksonXmlProperty(isAttribute = true)
    private String size = null;

    @JacksonXmlProperty(isAttribute = true)
    private String commandID = null;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlProperty(localName = "Player")
    private Player player = null;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Timeline> Timeline = null;

    // Getter and Setter methods for each field...

    public void setTimeline(List<com.pragmaticaudio.plexservice.controllers.entities.Timeline> timeline) {
        Timeline = timeline;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }
}