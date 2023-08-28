package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexCloudService;
import com.pragmaticaudio.plexservice.PlexMediaPlayer;
import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
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

        Boolean paused = getSingleParamValueAsBoolean(requestInfo,"paused");
        Boolean includeExternalMedia = getSingleParamValueAsBoolean(requestInfo,"includeExternalMedia");

        String source = getSingleParamValue(requestInfo, "source");

        String protocol = getSingleParamValue(requestInfo, "protocol");
        String address = getSingleParamValue(requestInfo, "address");
        String port = getSingleParamValue(requestInfo, "port");
        String token = getSingleParamValue(requestInfo, "token");
        String type = getSingleParamValue(requestInfo, "type");

        String key = getSingleParamValue(requestInfo, "key");
        String containerKey = getSingleParamValue(requestInfo, "containerKey");
        String machineIdentifier = getSingleParamValue(requestInfo, "machineIdentifier");
        Long offset = getSingleParamValueAsLong(requestInfo, "offset");

        String commandID = getSingleParamValue(requestInfo, "commandID");

        String serverURI = PlexCloudService.formatServerURI(protocol, address, port);

        PlayQueueMediaContainer playQueueMediaContainer = plexCloudService.loadMediaFromPlexServer(serverURI, token, containerKey);

        // So assuming we haven't seen an exception - lets delegate this PlayQueue entity to PlexMediaPlayer to start playing
        PlexMediaPlayer plexMediaPlayer = getMediaPlayer();
        plexMediaPlayer.createOrReplaceQueue(playQueueMediaContainer, serverURI, token);

        return "";  // Seems to be an empty response if successful
    }

}
