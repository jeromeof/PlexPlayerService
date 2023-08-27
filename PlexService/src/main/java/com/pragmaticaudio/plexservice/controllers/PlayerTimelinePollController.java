package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.util.Map;

@RestController("/player/timeline/poll")
public class PlayerTimelinePollController extends BaseController {

    @GET
    @Produces(ContentType.TEXT_XML_UTF8)
    public String poll(RequestInfo requestInfo, ResponseInfo responseInfo) {

        Map<String, String> headers = getAllPlexHeaders(requestInfo);

        logMessage("Timeline Poll - called: with headers:" + headers.toString());

        return OK_XML_RESPONSE;
    }

}
