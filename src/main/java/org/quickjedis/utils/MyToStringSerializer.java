package org.quickjedis.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class MyToStringSerializer extends JsonSerializer<Object> {
    public static final MyToStringSerializer instance = new MyToStringSerializer();

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value.getClass() == Double.class) {
            //Double 不使用科学计数法，可保留最大精度16位（小数点前位数+小数点后位数=16）
            jgen.writeNumber(new BigDecimal(value.toString()).toPlainString());
        } else if (value.getClass() == Float.class) {
            //Float 不使用科学计数法，可保留最大精度8位（小数点前位数+小数点后位数=8）
            jgen.writeNumber(new BigDecimal(value.toString()).toPlainString());
        } else {
            jgen.writeNumber(((BigDecimal) value).toPlainString());
        }
    }

}