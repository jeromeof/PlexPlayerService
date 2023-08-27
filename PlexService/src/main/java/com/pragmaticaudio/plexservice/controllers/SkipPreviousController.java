package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

@RestController("/player/playback/skipPrevious")
public class SkipPreviousController extends BaseController {

    @GET
    @Produces(ContentType.TEXT_XML)
    public MediaContainer skipPrevious(RequestInfo requestInfo, ResponseInfo responseInfo) {
        String type = getSingleParamValue(requestInfo, "type");
        String commandID = getSingleParamValue(requestInfo, "commandID");

        return new MediaContainer();
    }

}
