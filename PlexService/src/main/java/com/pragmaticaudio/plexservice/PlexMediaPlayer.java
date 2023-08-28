package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.Timeline;
import com.pragmaticaudio.plexservice.controllers.entities.Track;

import java.io.IOException;

/**
 * This represents the media actually playing - it will have concrete implementations in different Android players
 */
public abstract class PlexMediaPlayer {

    private Timeline timeline = null;

    private int currentQueueIndex = 0;  // Assume we start at the top of the queue
    private PlayQueueMediaContainer playQueueMediaContainer = null;
    private String token; // To use with the playQueue
    private String serverURI; // Server where the playqueue is located.

    public void reset() {
        currentQueueIndex = 0;
    }

    protected boolean moveNextTrack() { // True if successful otherwise end of queue
        if (currentQueueIndex >= playQueueMediaContainer.getSize()) {
            return false;
        }

        currentQueueIndex++;

        return true;
    }

    protected String getNextTrack() {
        if (playQueueMediaContainer.getSize() > currentQueueIndex ) {
            return createStreamFromTrack(playQueueMediaContainer.getTrack().get(currentQueueIndex));
        }

        return null;    // No current track
    }

    private String createStreamFromTrack(Track track) {
        return PlexCloudService.createStreamURL(serverURI, track.getKey(), token);
    }

    protected String getCurrentTrack() {
        if (playQueueMediaContainer.getSize() > currentQueueIndex ) {
            return createStreamFromTrack(playQueueMediaContainer.getTrack().get(currentQueueIndex));
        }

        return null;    // No current track
    }

    public Timeline getCurrentTimeline() {
        return this.timeline;
    }

    // All the various media operations
    public abstract void pause();

    public abstract void play() throws IOException;

    public abstract void skipNext() throws IOException;

    public abstract void stop();

    public abstract void skipPrevious();

    public abstract void changeVolume(Integer volume);

    public abstract void seekTo(Integer seekOffset) ;

    public void createOrReplaceQueue(PlayQueueMediaContainer playQueueMediaContainer,  String serverURI, String token) {
        this.playQueueMediaContainer = playQueueMediaContainer;
        this.serverURI = serverURI;
        this.token = token;
    }
}
