package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.plexservice.controllers.entities.Track;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class PlexCloudServiceTest extends TestCase {

    PlexCloudService plexCloudService = new PlexCloudService();

    @Test
    public void testGetPin() throws IOException {
        PlexPlayerInfo playerInfo = new PlexPlayerInfo("UnitTestPlayer","PlexPlayer", 32501, "123123-123123234-2344");
        PlexPinInfo pinInfo = plexCloudService.getPin(playerInfo);
        // Breakpoint between these lines and link the PIN to your account then continue
        PlexPinInfo plexPinInfo = plexCloudService.checkPin(pinInfo.getId(), playerInfo);

        Assert.assertNotNull(plexPinInfo.getAuthToken());
    }

    @Test
    @Ignore // Add your own details here to test
    public void testLoadMediaFromPlexServer() {

        String serverURI="https://XXXXXXXXX.plex.direct:32400";
        String token = "transient-123123123123213123";
        PlayQueueMediaContainer playQueueMediaContainer = plexCloudService.loadMediaFromPlexServer(serverURI,
                token,
                "%2FplayQueues%2F2795%3Fown%3D1");

        // Get stream from first track as a test
        if (playQueueMediaContainer != null && playQueueMediaContainer.getTrack() != null && playQueueMediaContainer.getTrack().size() > 0) {
            Track track = playQueueMediaContainer.getTrack().get(0);
            String trackStream = PlexCloudService.createStreamURL(serverURI, token, track.getMedia().getPart().getKey());

            System.out.println(trackStream);
        }

    }
}