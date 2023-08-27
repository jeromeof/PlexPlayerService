package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexCloudService;
import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

@RestController("/player/playback/playMedia")
public class PlayMediaController extends BaseController {

    private PlexCloudService plexCloudService = new PlexCloudService();

    @GET
    @Produces(ContentType.TEXT_XML)
    public String playMedia(RequestInfo requestInfo, ResponseInfo responseInfo) {
        validateStandardPlexHeaders(requestInfo);

        // Eg. source=35c96b643f0542be13e332f377fcdda6d4fa7dd0
        //     containerKey=%2FplayQueues%2F2795%3Fown%3D1
        //     key=%2Flibrary%2Fmetadata%2F118055
        //     offset=9797
        //     token=transient-d8dfa2fc-bcb3-4e3a-a8a0-2bc747d6ac2e
        //     includeExternalMedia=1
        //     type=music
        //     protocol=https
        //     address=192-168-31-201.f2a4a32b24354350827fe07f9d3d9865.plex.direct
        //     port=32400
        //     machineIdentifier=35c96b643f0542be13e332f377fcdda6d4fa7dd0
        //     commandID=13
        Boolean paused = getSingleParamValueAsBoolean(requestInfo,"paused");
        Boolean includeExternalMedia = getSingleParamValueAsBoolean(requestInfo,"includeExternalMedia");

        String source = getSingleParamValue(requestInfo, "source");

        String protocol = getSingleParamValue(requestInfo, "protocol");
        String address = getSingleParamValue(requestInfo, "address");
        Long port = getSingleParamValueAsLong(requestInfo, "port");
        String token = getSingleParamValue(requestInfo, "token");
        String type = getSingleParamValue(requestInfo, "type");

        String key = getSingleParamValue(requestInfo, "key");
        String containerKey = getSingleParamValue(requestInfo, "containerKey");
        String machineIdentifier = getSingleParamValue(requestInfo, "machineIdentifier");
        Long offset = getSingleParamValueAsLong(requestInfo, "offset");

        String commandID = getSingleParamValue(requestInfo, "commandID");

        String cloudServer = protocol + ":" + address + ":" + port;
        plexCloudService.loadMediaFromPlexServer(cloudServer, token, machineIdentifier, key, containerKey, type);

        return "";  // Seems to be an empty response if successful
    }

}
