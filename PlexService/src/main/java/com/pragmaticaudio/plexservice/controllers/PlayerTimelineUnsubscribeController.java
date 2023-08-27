package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.util.Map;

@RestController("/player/timeline/unsubscribe")
public class PlayerTimelineUnsubscribeController extends BaseController {

    @GET
    @Produces(ContentType.APPLICATION_XML)
    public String unsubscribe(RequestInfo requestInfo, ResponseInfo responseInfo) {
        Map<String, String> headers = getAllPlexHeaders(requestInfo);

        logMessage("Unsubscribe - called: with headers:" + headers.toString());

        return OK_XML_RESPONSE;
    }

}
