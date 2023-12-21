package org.quickjedis.utils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class MyToStringSerializer extends SerializerBase<Object> {
    public static final ToStringSerializer instance = new ToStringSerializer();

    public MyToStringSerializer() {
        super(Object.class);
    }

    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if(value.getClass() == BigDecimal.class){
            jgen.writeString(((BigDecimal) value).toPlainString());
        }else {
            jgen.writeString(value.toString());
        }
    }

    public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForScalar(value, jgen);
        this.serialize(value, jgen, provider);
        typeSer.writeTypeSuffixForScalar(value, jgen);
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return this.createSchemaNode("string", true);
    }
}