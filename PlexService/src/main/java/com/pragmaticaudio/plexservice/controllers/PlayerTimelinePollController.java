package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexMediaPlayer;
import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.StateEnum;
import com.pragmaticaudio.plexservice.controllers.entities.Timeline;
import com.pragmaticaudio.plexservice.controllers.entities.TypeEnum;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

@RestController("/player/timeline/poll")
public class PlayerTimelinePollController extends BaseController {


    @GET
    @Produces(ContentType.TEXT_XML_UTF8)
    public MediaContainer poll(RequestInfo requestInfo, ResponseInfo responseInfo) {

        validateStandardPlexHeaders(requestInfo);

        // Next get the fields from the URL:
        String wait = getParameter(requestInfo, "wait");
        String includeMetadata = getParameter(requestInfo, "includeMetadata");
        String commandID = getParameter(requestInfo, "commandID");
        String type = getParameter(requestInfo, "type");    // Should be music

        // So Wait is the strange command as we are suppose to wait until after the next command has
        // been responsed - typically another second before responding
        logMessage("Waiting for 1 second or next command to complete before continuing .. ");

        if (wait.compareToIgnoreCase("1") == 0) {
            waitFor1SecondOrNextTimeline();
        } else {
            wakeUpTimeline();
        }

        Map<String, String> headers = getAllPlexHeaders(requestInfo);

        addAllowHeadersToResponse(responseInfo);

        logMessage("Timeline Poll - called: with headers:" + headers.toString());


        // We have 3 timelines in a typical plexAmp response - but we only care about the music one
        List<Timeline> timelines = new LinkedList<>();

        // Get the MediaPlayer object as it can generate a timeline object from the current playing
        // item from the current playlist / stream
        PlexMediaPlayer mediaPlayer = getMediaPlayer();
        timelines.add(mediaPlayer.getCurrentTimeline());
        timelines.add(new Timeline(StateEnum.STOPPED.getValue(), TypeEnum.VIDEO.getValue()));
        timelines.add(new Timeline(StateEnum.STOPPED.getValue(), TypeEnum.PHOTO.getValue()));

        MediaContainer returnValue = new MediaContainer();
        returnValue.setCommandID(commandID);
        returnValue.setTimeline(timelines);

        return returnValue;
    }

    private Object monitor = new Object(); // Might need to be a map but a singleton for now
    private void waitFor1SecondOrNextTimeline() {
        synchronized (monitor) {
            try {
                monitor.wait(1000);  // Wait for 1 second or until notified
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore the interrupted status
            }
        }
    }
    private void wakeUpTimeline() {
        synchronized (monitor) {
            monitor.notify();  // Wake up the waiting thread
        }
    }


}
