package com.pragmaticaudio.plexservice;

import android.media.MediaPlayer;
import android.os.Handler;

import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.Track;

import java.io.IOException;
import java.net.URL;

public class AndroidPlexMediaPlayer extends PlexMediaPlayer {

    private Handler handler = new Handler();    // For the playback thread

    public AndroidPlexMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer nextMediaPlayer) {
        this.currentMediaPlayer = mediaPlayer;
        this.nextMediaPlayer = nextMediaPlayer;
    }
    private MediaPlayer currentMediaPlayer = null; // By default lets use the android mediaplayer - possible a vendor might have a custom alternative
    private MediaPlayer nextMediaPlayer = null;   // For gapless playback this is setup to be the next stream


    @Override
    public void pause() {
        if (currentMediaPlayer.isPlaying()) {
            currentMediaPlayer.pause();
        }
    }

    private final Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            if(currentMediaPlayer != null && currentMediaPlayer.isPlaying()) {
                int currentPosition = currentMediaPlayer.getCurrentPosition();
                // Update the UI using the currentPosition
                onUpdateTimeline(currentPosition);

                // Update UI with progress of track
                updateProgressUI(currentPosition);

                // Schedule the next update after a fixed interval (e.g., 1 second)
                handler.postDelayed(this, 1000);
            }
        }
    };


    private void updateProgressUI(int currentPosition) {
        // Here you can update progress bars, time labels, etc.
        // For instance, if you have a SeekBar:
        // yourSeekBar.setProgress(currentPosition);
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

            // Start the UI updates when media is prepared and starts playing
            handler.post(updateUI);

        });
        currentMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop updates when playback is complete
                handler.removeCallbacks(updateUI);
            }
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
        // Stop playing curret track
        stop();
        // Tell the base class we have moved to next track - so it now the current track
        if (moveNextTrack()) {
            // Prepare the next track
            setupNextTrack();

            play(); // Start playing
        }
    }

    @Override
    public void stop() {
        if (currentMediaPlayer.isPlaying()) {
            currentMediaPlayer.stop();
            try {
                currentMediaPlayer.prepare();  // prepare for playback
                currentMediaPlayer.seekTo(0);  // if you want to start the stream from the beginning
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void skipPrevious() throws IOException {
        if (currentMediaPlayer.isPlaying()) {
            // If we are near the start of the track skip to previous track and setup it up as current track and get next track again
            if (currentMediaPlayer.getCurrentPosition() < 30) {

                stop(); // Stop what we are doing !!

                movePrevTrack();

                setupNextTrack();

                play(); // Start playing again
            } else {
                currentMediaPlayer.seekTo(0);    // Just goto the start of the track
            }
        }
    }

    @Override
    public void changeVolume(Integer volume) {
        if (currentMediaPlayer.isPlaying()) {
            currentMediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public void seekTo(Integer seekOffset) {
        if (currentMediaPlayer.isPlaying()) {
            currentMediaPlayer.seekTo(seekOffset);
        }
    }

    @Override
    protected void onStartPlayback(Track track) throws IOException {
        play();
        
        // Update the UI with track metadata
        showTrackDetails(track);
    }

    private void showTrackDetails(Track track) {
    }

    @Override
    protected void onNewPlayQueue(PlayQueueMediaContainer playQueueMediaContainer) throws IOException {
        // Update the UI showing new Queue

    }

    // Now override each of the base class calls to talk to the real mediaplayer
}
