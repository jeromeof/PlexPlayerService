package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.BaseController;
import com.pragmaticaudio.plexservice.controllers.PauseController;
import com.pragmaticaudio.plexservice.controllers.PlayController;
import com.pragmaticaudio.plexservice.controllers.PlayMediaController;
import com.pragmaticaudio.plexservice.controllers.PlayerTimelinePollController;
import com.pragmaticaudio.plexservice.controllers.PlayerTimelineSubscribeController;
import com.pragmaticaudio.plexservice.controllers.PlayerTimelineUnsubscribeController;
import com.pragmaticaudio.plexservice.controllers.ResourcesController;
import com.pragmaticaudio.plexservice.controllers.SeekToController;
import com.pragmaticaudio.plexservice.controllers.SetParametersController;
import com.pragmaticaudio.plexservice.controllers.SkipNextController;
import com.pragmaticaudio.plexservice.controllers.SkipPreviousController;
import com.pragmaticaudio.plexservice.controllers.StopController;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.restserver.annotations.RestServer;
import com.pragmaticaudio.restserver.server.BaseRestServer;

import java.io.IOException;
import java.util.Map;

@RestServer( port = PlexPlayerServer.DEFAULT_PORT,
        converter = XmlConverter.class, //Optional
        authentication = PlexAuthentication.class, //Optional
        controllers = {
                ResourcesController.class,
                PlayerTimelinePollController.class,
                PlayerTimelineUnsubscribeController.class,
                PlayerTimelineSubscribeController.class,
                PlayMediaController.class,
                SetParametersController.class,
                SkipNextController.class,
                SkipPreviousController.class,
                StopController.class,
                SeekToController.class,
                PlayController.class,
                PauseController.class
        } )
public class PlexPlayerServer extends BaseRestServer {
    public static final int DEFAULT_PORT = 32500;   // For annotation - but overridable in the settings

    private final Map<String, Object> settings;         // Initially just store this here
    private final PlexPlayerLogger logger;
    private final MediaPlayer mediaPlayer;

    private final PlexCloudService plexCloudService = new PlexCloudService();

    public PlexPlayerServer(Map<String, Object> settings, PlexPlayerLogger logger, MediaPlayer mediaPlayer) {
        super();
        this.settings = settings;   // Save settings so Controllers can use them
        this.logger = logger;
        this.mediaPlayer = mediaPlayer;
    }

    public void start() throws IOException {

        // Lets set ourselves into the Controllers so they have easy access to common objects
        Map<String, Object> controllers = getControllers();
        for (Object controller : controllers.values()) {
            // I am too used to dependency injection so I manipulated the RestServer code
            // a little.
            if (controller instanceof BaseController) {
                ((BaseController)controller).setPlexPlayerServer(this);
            }
        }

        // start our backend thread which keeps the plex server update to date with our IP address:
        updatePlexTvConnection();

        super.start();
    }

    public int getPort() {
        // Get a Port from the settings if there is one - other default base class port config
        PlexPlayerInfo plexPlayerInfo = new PlexPlayerInfo(settings);
        if (plexPlayerInfo.getPort() != null) {
            return Integer.valueOf(plexPlayerInfo.getPort());
        }
        return super.getPort();
    }

    public void updatePlexTvConnection() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    plexCloudService.updatePlexTVDeviceDetails(new PlexPlayerInfo(settings), (String) settings.get("token"));

                    Thread.sleep(60000); // Sleep for 60 seconds then update again
                }

            } catch (Exception e) {
                // Handle exceptions
            }
        });
        thread.start();
    }
    public Map<String, Object> getSettings() {
        return settings;
    }

    public void logMessage(String message) {
        logger.logMessage(message);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}