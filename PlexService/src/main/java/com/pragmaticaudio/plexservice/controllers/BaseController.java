package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexPlayerServer;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.restserver.annotations.ExceptionHandler;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {


    public static final String X_PLEX_TARGET_CLIENT_IDENTIFIER = "Target-Client-Identifier";
    public static final String X_PLEX_CLIENT_IDENTIFIER = "Client-Identifier";
    public static final String X_PLEX_DEVICE_NAME = "Device-Name";
    private static final String PLEX_HEADER_PREFIX = "X-Plex-";

    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    public static final String OK_XML_RESPONSE = XML_HEADER + "<Response code=\"200\" status=\"OK\"/>";

    private PlexPlayerServer plexPlayerServer=null;

    protected String getTargetClientIdentifier(RequestInfo requestInfo) {
        return  getPlexHeader(requestInfo, (X_PLEX_TARGET_CLIENT_IDENTIFIER));
    }
    protected String getClientIdentifier(RequestInfo requestInfo) {
        return getPlexHeader(requestInfo, X_PLEX_CLIENT_IDENTIFIER);
    }

    protected void addPlexHeaderToResponse(ResponseInfo responseInfo, String headerName, String value) {
        responseInfo.addHeader(PLEX_HEADER_PREFIX + headerName, value );
    }

    protected String getPlexHeader(RequestInfo requestInfo, String headerName) {
        String result = requestInfo.getHeaders().get(PLEX_HEADER_PREFIX + headerName);
        if (result == null) {   // Try lowercase
            result = requestInfo.getHeaders().get((PLEX_HEADER_PREFIX + headerName).toLowerCase());
        }
        return result;
    }

    protected String getDeviceNme(RequestInfo requestInfo) {
        return getPlexHeader(requestInfo, X_PLEX_DEVICE_NAME);
    }

    protected String getPlexParameter(RequestInfo requestInfo, String fieldName) {
        List<String> strings = requestInfo.getParameters().get(PLEX_HEADER_PREFIX + fieldName);
        if (strings != null) {
            return strings.get(0);  // Just the first for now
        }
        return null;
    }

    protected void logMessage(String message) {
        plexPlayerServer.logMessage(message); // Delegate this back to the player
    }

    /**
     * Simple helper to get all the Plex headers and tidy the names up
     * @param requestInfo
     * @return
     */
    protected Map<String, String> getAllPlexHeaders(RequestInfo requestInfo) {
        Map<String, String> returnValues = new HashMap<>();
        Map<String, String> headers = requestInfo.getHeaders();
        for (String header : headers.keySet()) {
            if (isPlexHeader(header)) {
                returnValues.put(header.substring(PLEX_HEADER_PREFIX.length()), headers.get(header) );
            }
        }
        return returnValues;
    }

    private boolean isPlexHeader(String header) {
        if (header.length() <= PLEX_HEADER_PREFIX.length()) {
            return false;
        }
        if (header.toLowerCase().startsWith(PLEX_HEADER_PREFIX.toLowerCase())) {
            return true;
        }
        return false;
    }

    protected Boolean getSingleParamValueAsBoolean(RequestInfo requestInfo, String paramName) {
        String value = getSingleParamValue(requestInfo, paramName);
        Boolean returnVal = null;
        if (value != null) {
            if (value.length() > 1) {   // Assume true / false
                returnVal = Boolean.valueOf(value);
            }  else {
                returnVal = Integer.valueOf(value) == 1; // 1 is true - is false
            }
        }
        return returnVal;
    }

    protected Long getSingleParamValueAsLong(RequestInfo requestInfo, String paramName) {
        String value = getSingleParamValue(requestInfo, paramName);
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }

    protected String getSingleParamValue(RequestInfo requestInfo, String paramName) {
        Map<String, List<String>> parameters = requestInfo.getParameters();

        List<String> values = parameters.get(paramName);

        if (values == null) {
            return null;
        }
        return values.get(0);   // Only get the first
    }

    private static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public void setPlexPlayerServer(PlexPlayerServer plexPlayerServer) {
        this.plexPlayerServer = plexPlayerServer;
    }

    public PlexPlayerInfo getPlayerInfo() {
        return new PlexPlayerInfo(plexPlayerServer.getSettings());
    }


    @ExceptionHandler
    @Produces(ContentType.TEXT_PLAIN)
    public void handleThrowable(Throwable throwable, ResponseInfo response) {
        String throwableStr = getStackTrace(throwable);
        response.setBody(throwableStr.getBytes());
    }
}
