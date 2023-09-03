package com.pragmaticaudio.plexservice.controllers.entities;

import com.pragmaticaudio.plexservice.PlexPlayerDaemon;

import java.util.Date;
import java.util.Map;

public class PlexPlayerInfo {

    public static final String PLEX_TOKEN =   "plex_token";

    private String name;
    private String port;
    private String contentType = "plex/media-player";

    private String platform = "android";

    private String device = "DAP";
    private String product;
    private String protocol = "plex";
    private String protocolVersion = "1";
    private String protocolCapabilities = "timeline,playback,playqueues";

    private String deviceCapabilties = "player,pubsub-player";   // When registering this device into list of devices
    private String version = "1";   // Keep everything at 1 for now
    private String resourceIdentifier;
    private String deviceClass = "stb";
    private String platformVersion = "1";

    private String ourIpAddress = null;

    // Constructors
    public PlexPlayerInfo(String name, String product, int port, String resourceIdentifier) {
        this.name = name;
        this.product = product;
        this.port = String.valueOf(port);
        this.resourceIdentifier = resourceIdentifier;
    }

    public PlexPlayerInfo(Map<String, Object> settings) {
        if (settings == null || !settings.containsKey("playerInfo")) {
            throw new IllegalArgumentException("Invalid settings provided - no playerInfo.");
        }

        Map<String, Object> playerInfoMap = (Map<String, Object>) settings.get("playerInfo");

        this.name = extractStringValue(playerInfoMap, "name");
        this.product = extractStringValue(playerInfoMap, "product");
        this.port = String.valueOf(extractIntValue(playerInfoMap, "port"));
        this.resourceIdentifier = extractStringValue(playerInfoMap, "resourceIdentifier");

        // If we have an ipAddress in the settings use it
        this.ourIpAddress = (String) settings.get("ourIpAddress");
    }

    private String extractStringValue(Map<String, Object> map, String key) {
        if (!map.containsKey(key) || !(map.get(key) instanceof String)) {
            throw new IllegalArgumentException("Invalid or missing value for key: " + key);
        }
        return (String) map.get(key);
    }

    private int extractIntValue(Map<String, Object> map, String key) {
        if (!map.containsKey(key) || !(map.get(key) instanceof Integer)) {
            throw new IllegalArgumentException("Invalid or missing value for key: " + key);
        }
        return (int) map.get(key);
    }
    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getProtocolCapabilities() {
        return protocolCapabilities;
    }

    public void setProtocolCapabilities(String protocolCapabilities) {
        this.protocolCapabilities = protocolCapabilities;
    }

    public String getDeviceCapabilties() {
        return deviceCapabilties;
    }

    public void setDeviceCapabilties(String deviceCapabilties) {
        this.deviceCapabilties = deviceCapabilties;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    public long getUpdatedAt() {
        return (long)(new Date().getTime() / 1000) ;
    }   // Always return the latest time here

    public String getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOurIpAddress() {
        return ourIpAddress;
    }

    public void setOurIpAddress(String ourIpAddress) {
        this.ourIpAddress = ourIpAddress;
    }

    @Override
    public String toString() {
        return "PlexPlayerInfo{" +
                "name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", contentType='" + contentType + '\'' +
                ", product='" + product + '\'' +
                ", protocol='" + protocol + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", protocolCapabilities='" + protocolCapabilities + '\'' +
                ", version='" + version + '\'' +
                ", resourceIdentifier='" + resourceIdentifier + '\'' +
                ", deviceClass='" + deviceClass + '\'' +
                '}';
    }
    
    public String generateGDMHelloPayload() {
        return "HELLO * HTTP/1.0\n" +
                generateBroadcastPayload();
    }

    public String generateGDMResponse() {
        return "HTTP/1.0 200 OK\n" +
                generateBroadcastPayload();
    }
    private String generateBroadcastPayload() {
        return  "Content-Type: " + contentType + '\n' +
                "Port: " + port + '\n' +
                "Protocol: " + protocol + '\n' +
                "Resource-Identifier: " + resourceIdentifier  + '\n' +
                "Name: " + name + '\n' +
                "Version: " + version + '\n' +
                "Product: " + product + '\n' +
                "Protocol-Version: " + protocolVersion + '\n' +
                "Protocol-Capabilities: " + protocolCapabilities + '\n' +
                "Updated-At: " + getUpdatedAt() + '\n' +
                "Device-Class: " + deviceClass + '\n';
    }


}