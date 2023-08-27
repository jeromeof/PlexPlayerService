package com.pragmaticaudio.plexservice.controllers.entities;

import java.net.InetAddress;

public class PlexServerInfo {

    private String contentType;
    private String host;
    private String name;
    private String port;
    private String resourceIdentifier;
    private long updatedAt;
    private String version;
    private String hostAddress; // Local network host address

    // Constructors
    public PlexServerInfo() {
    }

    public PlexServerInfo(InetAddress address, String gdmPacketData) {

        String[] lines = gdmPacketData.split("\n");
        for (String line : lines) {
            String[] parts = line.split(": ", 2);  // Split by the first occurrence of ": "
            if (parts.length < 2) continue;       // Skip malformed lines

            String headerName = parts[0].trim();
            String headerValue = parts[1].trim();

            switch (headerName) {
                case "Content-Type":
                    this.setContentType(headerValue);
                    break;
                case "Host":
                    // Host is the ipaddress of the local plex server appended with the value
                    String hostAddress = address.getHostAddress();
                    String modifiedHostAddress = hostAddress.replace(".", "-");

                    this.setHost("https://" + modifiedHostAddress + "." + headerValue);
                    this.setLocalNetworkServer(hostAddress);
                    break;
                case "Name":
                    this.setName(headerValue);
                    break;
                case "Port":
                    this.setPort(headerValue);
                    break;
                case "Resource-Identifier":
                    this.setResourceIdentifier(headerValue);
                    break;
                case "Updated-At":
                    this.setUpdatedAt(Long.parseLong(headerValue));
                    break;
                case "Version":
                    this.setVersion(headerValue);
                    break;
            }
        }
    }

    private void setLocalNetworkServer(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    // Getters and Setters
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

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

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "contentType='" + contentType + '\'' +
                ", host='" + host + '\'' +
                ", hostAddress='" + hostAddress + '\'' +
                ", name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", resourceIdentifier='" + resourceIdentifier + '\'' +
                ", updatedAt=" + updatedAt +
                ", version='" + version + '\'' +
                '}';
    }
}