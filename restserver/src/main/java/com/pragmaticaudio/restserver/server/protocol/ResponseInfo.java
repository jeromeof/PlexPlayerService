package com.pragmaticaudio.restserver.server.protocol;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.pragmaticaudio.restserver.server.dictionary.ContentType;
import com.pragmaticaudio.restserver.server.dictionary.ResponseStatus;

public class ResponseInfo {

    /**
     * Статус
     */
    private ResponseStatus status = ResponseStatus.OK;

    /**
     * Тип
     */
    private String type = ContentType.TEXT_PLAIN;

    private Map<String, String> headers = new HashMap<>();

    /**
     * Тело запроса
     */
    private byte[] body;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getBodyInputStream() {
        if (body != null)
            return new ByteArrayInputStream(body);

        return null;
    }

    public int getBodyLength() {
        if (body != null)
            return body.length;

        return 0;
    }
}
