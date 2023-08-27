package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.MediaPlayer;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

@RestController("/player/playback/skipNext")
public class SeekToController extends BaseController {

    @GET
    @Produces(ContentType.TEXT_XML)
    public String seekTo(RequestInfo requestInfo, ResponseInfo responseInfo) {
        String type = getSingleParamValue(requestInfo, "type");
        String commandID = getSingleParamValue(requestInfo, "commandID");
        validateStandardPlexHeaders(requestInfo);
        String offset = getSingleParamValue(requestInfo, "offset");

        MediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.seekTo(Integer.valueOf(offset));

        return "";
    }

}
