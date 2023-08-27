package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.Timeline;

/**
 * This represents the media actually playing - it will have concrete implementations in different Android players
 */
public abstract class MediaPlayer {

    private Timeline timeline = null;

    public Timeline getCurrentTimeline() {
        return this.timeline;
    }

    public void pause() {

    }

    public void play() {
    }

    public void skipNext() {
    }

    public void stop() {
    }

    public void skipPrevious() {
    }

    public void changeVolume(Integer volume) {

    }

    public void seekTo(Integer seekOffset) {

    }
}
