package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

@RestController("/player/playback/playMedia")
public class PlayMediaController extends BaseController {

    @GET
    @Produces(ContentType.TEXT_XML)
    public MediaContainer playMedia(RequestInfo requestInfo, ResponseInfo responseInfo) {
        Boolean paused = getSingleParamValueAsBoolean(requestInfo,"paused");
        Boolean includeExternalMedia = getSingleParamValueAsBoolean(requestInfo,"includeExternalMedia");
        String source = getSingleParamValue(requestInfo, "source");
        String protocol = getSingleParamValue(requestInfo, "protocol");
        String address = getSingleParamValue(requestInfo, "address");
        Long port = getSingleParamValueAsLong(requestInfo, "port");
        String token = getSingleParamValue(requestInfo, "token");
        String type = getSingleParamValue(requestInfo, "type");
        String containerKey = getSingleParamValue(requestInfo, "containerKey");
        String key = getSingleParamValue(requestInfo, "key");
        String machineIdentifier = getSingleParamValue(requestInfo, "machineIdentifier");
        String commandID = getSingleParamValue(requestInfo, "commandID");
        Long offset = getSingleParamValueAsLong(requestInfo, "offset");

        return new MediaContainer();
    }

}
