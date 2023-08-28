package com.pragmaticaudio.plexservice.controllers;

import com.pragmaticaudio.plexservice.PlexMediaPlayer;
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

    protected String getDeviceName(RequestInfo requestInfo) {
        return getPlexHeader(requestInfo, X_PLEX_DEVICE_NAME);
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
    protected void addAllowHeadersToResponse(ResponseInfo responseInfo) {
        responseInfo.addHeader("Access-Control-Allow-Origin", "*");
        responseInfo.addHeader("Access-Control-Max-Age", "1209600");
        responseInfo.addHeader("Access-Control-Expose-Headers","X-Plex-Client-Identifier");
        responseInfo.addHeader("Access-Control-Allow-Private-Network", "true");
        responseInfo.addHeader("Access-Control-Allow-Headers", "*");
    }

    protected String getPlexParameter(RequestInfo requestInfo, String fieldName) {
        String param = PLEX_HEADER_PREFIX + fieldName;
        return getParameter(requestInfo, param);
    }

    protected String getParameter(RequestInfo requestInfo, String param) {
        List<String> strings = requestInfo.getParameters().get(param);
        if (strings != null) {
            return strings.get(0);
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

    protected PlexMediaPlayer getMediaPlayer() {
        return plexPlayerServer.getMediaPlayer();
    }


    protected void validateStandardPlexHeaders(RequestInfo requestInfo) {
        // Get the headers from the header request
        String targetClientIdentifier = getTargetClientIdentifier(requestInfo);
        String clientIdentifier = getClientIdentifier(requestInfo);
        String deviceName = getDeviceName(requestInfo);

        // validate that they are correct for us e.g

    }


}
