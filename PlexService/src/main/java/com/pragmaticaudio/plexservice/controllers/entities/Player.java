package com.pragmaticaudio.plexservice.controllers.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Player {

    @JacksonXmlProperty(isAttribute = true)
    private String deviceClass;

    @JacksonXmlProperty(isAttribute = true)
    private String machineIdentifier;

    @JacksonXmlProperty(isAttribute = true)
    private String platform;

    @JacksonXmlProperty(isAttribute = true)
    private String platformVersion;

    @JacksonXmlProperty(isAttribute = true)
    private String product;

    @JacksonXmlProperty(isAttribute = true)
    private String protocol;

    @JacksonXmlProperty(isAttribute = true)
    private String protocolCapabilities;

    @JacksonXmlProperty(isAttribute = true)
    private String protocolVersion;

    @JacksonXmlProperty(isAttribute = true)
    private String title;

    @JacksonXmlProperty(isAttribute = true)
    private String version;

    // Getter and Setter methods for each field...

    public String getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }

    public String getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
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

    public String getProtocolCapabilities() {
        return protocolCapabilities;
    }

    public void setProtocolCapabilities(String protocolCapabilities) {
        this.protocolCapabilities = protocolCapabilities;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}