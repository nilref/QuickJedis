package org.quickjedis.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;

public class JsonHelper {
    private static JacksonObjectMapper objectMapper = null;

    static {
        objectMapper = new JacksonObjectMapper();
        //将浮点型反序列化成BigDecimal
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    }

    /**
     * json字符串转成对象
     *
     * @param jsonStr
     * @param className
     * @return
     * @throws Exception
     */
    public static <T> T toObject(String jsonStr, Class<T> className) {
        try {
            return objectMapper.readValue(jsonStr, className);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串转成对象集合
     *
     * @param jsonStr
     * @param className
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String jsonStr, Class<T> className) {
        JavaType javaType = getCollectionType(List.class, className);
        try {
            return (List<T>) objectMapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象转换成json
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
