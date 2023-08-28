package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.PlayQueueMediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class to hold the various cloud services calls we need
 */
public class PlexCloudService {
    private static final String PINS = "https://plex.tv/pins.xml";
    private static final String CHECKPINS = "https://plex.tv/pins/%s.xml";
    private static final String DEVICES_UPDATE = "https://plex.tv/devices/%s?X-Plex-Token=%s";

    private static final String PLEX_SERVER_URI = "%s://%s:%s";
    private static final String LOADMEDIA_URL = "%s%s";
    private static final String STREAMING_MEDIA_URL = "%s%s?X-Plex-Token=%s";

    private static final String DEVICES_UPDATE_BODY = "Connection[][uri]=http://%s:%s";

    private OkHttpClient client = new OkHttpClient();

    public PlexPinInfo getPin(PlexPlayerInfo playerInfo) throws IOException {
        Request request = new Request.Builder()
                .url(PINS)
                .post(RequestBody.create(new byte[0])) // Empty POST request
                .headers(pmsHeaders(playerInfo))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }

            // Assuming we get a xml response - parse it and return
            String xmlResponse = response.body().string();

            XmlConverter xmlConverter = new XmlConverter();
            PlexPinInfo plexPinInfo = xmlConverter.readValue(xmlResponse.getBytes(), PlexPinInfo.class);
            return plexPinInfo;
        }
    }

    public PlexPinInfo checkPin(String pinId, PlexPlayerInfo playerInfo) throws IOException {
        String url = String.format(CHECKPINS, pinId);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(pmsHeaders(playerInfo))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }

            String xmlResponse = response.body().string();

            XmlConverter xmlConverter = new XmlConverter();
            PlexPinInfo plexPinInfo = xmlConverter.readValue(xmlResponse.getBytes(), PlexPinInfo.class);
            return plexPinInfo;
        }
    }
    private Headers pmsHeaders(PlexPlayerInfo playerInfo) {
        return pmsHeaders(playerInfo, null);
    }

    private Headers pmsHeaders(PlexPlayerInfo playerInfo, Map<String, String> extraHeaders) {
        Headers.Builder builder = new Headers.Builder();
        if (extraHeaders != null) {
            for (String header: extraHeaders.keySet()) {
                builder.add(header, extraHeaders.get(header));
            }
        }
        return builder
                .add("X-Plex-Client-Identifier", playerInfo.getResourceIdentifier())
                .add("X-Plex-Product", playerInfo.getProduct() )
                .add("X-Plex-Version", playerInfo.getVersion() )
                .add("X-Plex-Device", playerInfo.getProduct() )
                .add("X-Plex-Device-Name", playerInfo.getName() )
                .add("X-Plex-Platform", playerInfo.getPlatform() )
                .add("X-Plex-Platform-Version", playerInfo.getProtocolVersion() )
                .add("X-Plex-Provides", playerInfo.getDeviceCapabilties() )
        .build();
    }

    public void updatePlexTVDeviceDetails(PlexPlayerInfo plexPlayerInfo, String token) throws IOException {
        String url = String.format(DEVICES_UPDATE, plexPlayerInfo.getResourceIdentifier(), token);
        String body = String.format(DEVICES_UPDATE_BODY, plexPlayerInfo.getOurIpAddress(), plexPlayerInfo.getPort());
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(body, MediaType.get("application/x-www-form-urlencoded"))) // Empty POST request
                .headers(pmsHeaders(plexPlayerInfo))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
        }
    }

    public static String createStreamURL(String serverURI, String token, String trackKey) {
        return String.format(STREAMING_MEDIA_URL, serverURI, trackKey, token );
    }

    public PlayQueueMediaContainer loadMediaFromPlexServer(String serverURI, String token, String containerKey) {
        containerKey = URLDecoder.decode(containerKey); // Decode the container Key
        String url = String.format(LOADMEDIA_URL, serverURI, containerKey);

        if (containerKey.contains("?")) {
            url += "&X-Plex-Token=" + token;
        } else {
            url += "?X-Plex-Token=" + token;
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }

            String xmlResponse = response.body().string();

            XmlConverter xmlConverter = new XmlConverter();
            PlayQueueMediaContainer mediaContainer = xmlConverter.readValue(xmlResponse.getBytes(), PlayQueueMediaContainer.class);
            return mediaContainer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //
    }


    public static String formatServerURI(String protocol, String address, String port) {
        return String.format(PLEX_SERVER_URI, protocol, address, port);
    }
}
