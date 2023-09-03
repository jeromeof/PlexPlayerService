package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.Track;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MockMediaPlayer extends PlexMediaPlayer {

    private PlayQueueMediaContainer playQueueMediaContainer = null;
    @Override
    public void pause() {
        System.out.println("Paused called");
    }

    @Override
    public void play() {
        System.out.println("Play called");
    }

    @Override
    public void skipNext() {
        System.out.println("Skip Next called");
    }

    @Override
    public void stop() {
        System.out.println("Stop called");
    }

    @Override
    public void skipPrevious() {
        System.out.println("Skip Previous called");

    }

    @Override
    public void changeVolume(Integer volume) {
        System.out.println("Change Volume called: " + volume);
    }

    @Override
    public void seekTo(Integer seekOffset) {
        System.out.println("Seek too offset:" + seekOffset);
    }

    @Override
    protected void onStartPlayback(Track track) throws IOException {
        AtomicInteger iCurrentPosition = new AtomicInteger();
        MockPlayback thread = new MockPlayback(() -> {
            onUpdateTimeline(iCurrentPosition.getAndIncrement());
        });

        thread.start();
    }

    @Override
    protected void onNewPlayQueue(PlayQueueMediaContainer playQueueMediaContainer) {
        // Save the playQueue details so we can mock real android behaviour here
        this.playQueueMediaContainer = playQueueMediaContainer;


    }

    /**
     * Thread to mock a playback of media
     */
    public class MockPlayback extends Thread {

        private boolean running = true;
        private final Runnable callback;

        public MockPlayback(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    // Sleep for 1 second
                    Thread.sleep(1000);

                    // Call the callback
                    callback.run();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // This method can be used to stop the thread gracefully
        public void stopThread() {
            running = false;
        }
    }
}
