package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.MediaContainer;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class TimelineSerialisationTest {

    XmlConverter xmlConverter = new XmlConverter();

    @Test
    public void testLoadingTimeline() throws IOException {
        String sampleFile = "sample-timeline.xml";
        // Get the class loader for the current class
        ClassLoader classLoader = getClass().getClassLoader();

        // Get the XML file as an InputStream
        InputStream inputStream = classLoader.getResourceAsStream(sampleFile);
        if (inputStream == null) {
            throw new AssertionError("Could not load the sample XML file.");
        }
        MediaContainer result = xmlConverter.readValue(convert(inputStream).getBytes(), MediaContainer.class);

        // Now compare the result with the original stream
    }

    public static String convert(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        }
    }


}
