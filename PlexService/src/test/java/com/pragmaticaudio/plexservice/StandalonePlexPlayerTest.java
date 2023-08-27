package com.pragmaticaudio.plexservice;

import org.junit.Test;
import java.io.IOException;

/**
 * A complete test of the Plex Server in plain java - to avoide the connect
 * connectivities issues of the Android simulator
 */
public class StandalonePlexPlayerTest {

    private StandalonePlexServer standalonePlexServer = new StandalonePlexServer();

    @Test
    public void testServer() throws IOException, InterruptedException {

        standalonePlexServer.startServer();

        standalonePlexServer.waitUntilFinished();

    }
}
