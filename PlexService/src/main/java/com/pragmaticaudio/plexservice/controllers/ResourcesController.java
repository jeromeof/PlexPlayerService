package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.Player;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.exceptions.NotFoundException;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.util.Map;

@RestController("/resources")
public class ResourcesController extends BaseController {

    @GET
    @Produces(ContentType.APPLICATION_XML)
    public MediaContainer resources(RequestInfo requestInfo, ResponseInfo responseInfo) throws NotFoundException {
        PlexPlayerInfo playerInfo = getPlayerInfo();

        String targetClientIdentifier = getTargetClientIdentifier(requestInfo);
        if (playerInfo.getResourceIdentifier().compareToIgnoreCase(targetClientIdentifier) != 0) {
            throw new NotFoundException();
        }

        String language = getPlexParameter(requestInfo, "Language");
        String deviceName = getPlexParameter(requestInfo, "Device-Name");

        Map<String, String> headers = getAllPlexHeaders(requestInfo);

        logMessage("Resources: Request from " + requestInfo.getRemoteIpAddress() +" with  with headers:" + headers.toString());

        // Add typical plex headers to response
        addPlexHeadersToResponse(responseInfo);

        MediaContainer returnValue = new MediaContainer();

        Player playerDetails = new Player();
        returnValue.setPlayer(playerDetails);
        playerDetails.setDeviceClass(playerInfo.getDeviceClass());  // stb
        playerDetails.setMachineIdentifier(playerInfo.getResourceIdentifier());
        playerDetails.setPlatformVersion(playerInfo.getVersion());
        playerDetails.setProduct(playerInfo.getProduct());
        playerDetails.setProtocol(playerInfo.getProtocol());
        playerDetails.setProtocolCapabilities(playerInfo.getProtocolCapabilities());
        playerDetails.setProtocolVersion(playerInfo.getProtocolVersion());
        playerDetails.setPlatform(playerInfo.getPlatform());
        playerDetails.setPlatformVersion(playerInfo.getPlatformVersion());
        playerDetails.setTitle(playerInfo.getName());
        playerDetails.setVersion(playerInfo.getProtocolVersion());  // Check this with documentation
        return returnValue;
    }

    private void addPlexHeadersToResponse(ResponseInfo responseInfo) {
        PlexPlayerInfo playerInfo = getPlayerInfo();
        addPlexHeaderToResponse(responseInfo,"Device", playerInfo.getName());
        addPlexHeaderToResponse(responseInfo,"Platform", "PlexPlayer"); // Check this out later
        addPlexHeaderToResponse(responseInfo,"Platform-Version", playerInfo.getVersion());
        addPlexHeaderToResponse(responseInfo,"Product", playerInfo.getProduct());
        addPlexHeaderToResponse(responseInfo,"Version", playerInfo.getProtocolVersion());
        addPlexHeaderToResponse(responseInfo,"Client-Identifier", playerInfo.getResourceIdentifier());
        addPlexHeaderToResponse(responseInfo,"Device-Name", playerInfo.getName());
        addPlexHeaderToResponse(responseInfo,"Provides", playerInfo.getDeviceCapabilties());
    }


}