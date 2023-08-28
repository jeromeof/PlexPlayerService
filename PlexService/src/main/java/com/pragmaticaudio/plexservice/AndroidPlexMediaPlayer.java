package com.pragmaticaudio.plexservice;

import android.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;

public class AndroidPlexMediaPlayer extends PlexMediaPlayer {

    public AndroidPlexMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer nextMediaPlayer) {
        this.currentMediaPlayer = mediaPlayer;
        this.nextMediaPlayer = nextMediaPlayer;
    }
    private MediaPlayer currentMediaPlayer = null; // By default lets use the android mediaplayer - possible a vendor might have a custom alternative
    private MediaPlayer nextMediaPlayer = null;   // For gapless playback this is setup to be the next stream


    @Override
    public void pause() {
        currentMediaPlayer.pause();
    }

    @Override
    public void play() throws IOException {

        if (currentMediaPlayer.isPlaying()) {
            return;     // Already playing
        }

        currentMediaPlayer.setDataSource(getCurrentTrack());
        currentMediaPlayer.prepareAsync();
        currentMediaPlayer.setOnPreparedListener(mp -> {
            currentMediaPlayer.start();
        });

        setupNextTrack();

    }

    private void setupNextTrack() throws IOException {
        nextMediaPlayer.setDataSource(getNextTrack());
        nextMediaPlayer.prepareAsync();
        currentMediaPlayer.setNextMediaPlayer(nextMediaPlayer);

        currentMediaPlayer.setOnCompletionListener(mp -> {
            currentMediaPlayer.release();

            // Swap around the 2 mediaplayer objects
            MediaPlayer swapAroundMediaPlayer = currentMediaPlayer;
            currentMediaPlayer = nextMediaPlayer;
            nextMediaPlayer = swapAroundMediaPlayer;
            currentMediaPlayer.setNextMediaPlayer(nextMediaPlayer);
        });
    }

    @Override
    public void skipNext() throws IOException {
        // Tell the base class we have moved to next track - so it now the current track
        if (moveNextTrack()) {
            // Prepare the next track
            setupNextTrack();
        }
    }

    @Override
    public void stop() {
        nextMediaPlayer.stop();
    }

    @Override
    public void skipPrevious() {

        // If we are near the start of the track skip to previous track and setup it up as current track and get next track again

        // If we are not near the start - then just reset the track position to the start of the track.

    }

    @Override
    public void changeVolume(Integer volume) {

    }

    @Override
    public void seekTo(Integer seekOffset) {

    }

    // Now override each of the base class calls to talk to the real mediaplayer
}
