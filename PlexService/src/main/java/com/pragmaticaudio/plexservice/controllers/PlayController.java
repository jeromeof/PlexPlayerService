package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexMediaPlayer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.io.IOException;

@RestController("/player/playback/play")
public class PlayController extends BaseController {

    @GET
    @Produces(ContentType.TEXT_XML)
    public void play(RequestInfo requestInfo, ResponseInfo responseInfo) throws IOException {
        String type = getSingleParamValue(requestInfo, "type");
        String commandID = getSingleParamValue(requestInfo, "commandID");
        validateStandardPlexHeaders(requestInfo);

        PlexMediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.play();

    }

}
