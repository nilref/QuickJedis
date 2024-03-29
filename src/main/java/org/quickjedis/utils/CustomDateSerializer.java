package org.quickjedis.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class CustomDateSerializer extends JsonSerializer<DateTime> {
    public static final CustomDateSerializer instance = new CustomDateSerializer();
    private static DateTimeFormatter formatter =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public void serialize(DateTime value, JsonGenerator gen,
                          SerializerProvider arg2)
            throws IOException {
        gen.writeString(formatter.print(value));
    }
}