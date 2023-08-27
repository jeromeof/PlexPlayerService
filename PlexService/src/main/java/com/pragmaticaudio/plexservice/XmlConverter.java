package com.pragmaticaudio.plexservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.pragmaticaudio.restserver.server.converter.BaseConverter;

import java.io.IOException;

public class XmlConverter implements BaseConverter {

    private XmlMapper mapper = new XmlMapper();
    @Override
    public byte[] writeValueAsBytes(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T readValue(byte[] src, Class<T> valueType) {
        try {
            return mapper.readValue(src, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
