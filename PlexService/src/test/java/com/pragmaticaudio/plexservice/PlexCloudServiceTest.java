package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;

import junit.framework.TestCase;

import org.junit.Assert;

import java.io.IOException;

public class PlexCloudServiceTest extends TestCase {

    PlexCloudService plexCloudService = new PlexCloudService();
    public void testGetPin() throws IOException {
        PlexPlayerInfo playerInfo = new PlexPlayerInfo("UnitTestPlayer","PlexPlayer", 32501, "123123-123123234-2344");
        PlexPinInfo pinInfo = plexCloudService.getPin(playerInfo);
        // Breakpoint between these lines and link the PIN to your account then continue
        PlexPinInfo plexPinInfo = plexCloudService.checkPin(pinInfo.getId(), playerInfo);

        Assert.assertNotNull(plexPinInfo.getAuthToken());
    }
}