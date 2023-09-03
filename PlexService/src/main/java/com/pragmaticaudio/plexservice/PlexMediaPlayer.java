package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.StateEnum;
import com.pragmaticaudio.plexservice.controllers.entities.Timeline;
import com.pragmaticaudio.plexservice.controllers.entities.Track;
import com.pragmaticaudio.plexservice.controllers.entities.TypeEnum;

import java.io.IOException;

/**
 * This represents the media actually playing - it will have concrete implementations in different Android players
 */
public abstract class PlexMediaPlayer {

    private static final String DEFAULT_CONTROLLABLE_LIST = "playPause,stop,volume,shuffle,repeat,seekTo,skipPrevious,skipNext,stepBack,stepForward";
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

    protected boolean movePrevTrack() { // True if successful otherwise start of queue
        if (currentQueueIndex == 0) {   // If we are at the start
            return false;
        }

        currentQueueIndex--;

        return true;
    }

    protected String getNextTrack() {
        if (playQueueMediaContainer.getSize() > (currentQueueIndex+1) ) {
            return createStreamFromTrack(playQueueMediaContainer.getTrack().get(currentQueueIndex+1));
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

    protected void onUpdateTimeline(int currentPosition) {
        Timeline currentTimeline = getCurrentTimeline();
        currentTimeline.setTime(""+currentPosition);
    }


    public Timeline getCurrentTimeline() {
        if (timeline == null) { // Create an blank timeline
            timeline = new Timeline(StateEnum.STOPPED.getValue(), TypeEnum.MUSIC.getValue());
            timeline.setControllable(DEFAULT_CONTROLLABLE_LIST);
        }
        return this.timeline;
    }

    public void updateTimeline(String state, Track trackDetails, int position, int volume, int mute, int shuffle) {
        Timeline currentTimeline = getCurrentTimeline();
        currentTimeline.setState(state);
        currentTimeline.setTime(String.valueOf(position));
        currentTimeline.setVolume(String.valueOf(volume));
        currentTimeline.setShuffle(String.valueOf(shuffle));
        currentTimeline.setRepeat(String.valueOf(shuffle));
        currentTimeline.setTrack(trackDetails);

        // Rest of the timeline are the track details itself
    }

    // All the various media operations
    public abstract void pause();

    public abstract void play() throws IOException;

    public abstract void skipNext() throws IOException;

    public abstract void stop();

    public abstract void skipPrevious() throws IOException;

    public abstract void changeVolume(Integer volume);

    public abstract void seekTo(Integer seekOffset) ;

    public void createOrReplaceQueue(PlayQueueMediaContainer playQueueMediaContainer,  String serverURI, String token) throws IOException {
        this.playQueueMediaContainer = playQueueMediaContainer;
        this.serverURI = serverURI;
        this.token = token;

        // Get the first track from the play queue and update the timeline object
        if (playQueueMediaContainer.getTrack().size() > 0) {
            currentQueueIndex = 0;
            Track track = playQueueMediaContainer.getTrack().get(currentQueueIndex);

            // Update timeline with our defaults - lets start playing by default
            updateTimeline(StateEnum.PLAYING.getValue(), track, 0, 0, 0, 0);

            // Lets setup the first track and update the UI etc
            onStartPlayback(track);
        }

        // Now we need to tell the implementation that we have a new playQueue and a new track for the timeline
        onNewPlayQueue(playQueueMediaContainer);


    }

    protected abstract void onStartPlayback(Track track) throws IOException;

    protected abstract void onNewPlayQueue(PlayQueueMediaContainer playQueueMediaContainer) throws IOException;
}
