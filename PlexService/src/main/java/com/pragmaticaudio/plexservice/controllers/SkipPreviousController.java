package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexMediaPlayer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.io.IOException;

@RestController("/player/playback/skipPrevious")
public class SkipPreviousController extends BaseController {

    @GET
    @Produces(ContentType.TEXT_XML)
    public void skipPrevious(RequestInfo requestInfo, ResponseInfo responseInfo) throws IOException {
        String type = getSingleParamValue(requestInfo, "type");
        String commandID = getSingleParamValue(requestInfo, "commandID");
        validateStandardPlexHeaders(requestInfo);

        PlexMediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.skipPrevious();

    }

}
