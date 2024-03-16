package org.quickjedis.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class CustomDateDeserializer extends JsonDeserializer<DateTime> {

    public static final CustomDateDeserializer instance = new CustomDateDeserializer();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public DateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateTimeString = p.getText();
        return dateTimeFormatter.parseDateTime(dateTimeString);
    }
}