package org.quickjedis.utils;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

public class JsonHelper {
	private static ObjectMapper objectMapper = null;
	static {
		objectMapper = new ObjectMapper();
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param jsonStr
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(String jsonStr, Class<T> className) throws Exception {
		return objectMapper.readValue(jsonStr, className);
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
	public static <T> List<T> toList(String jsonStr, Class<T> className) throws Exception {
		JavaType javaType = getCollectionType(List.class, className);
		return (List<T>) objectMapper.readValue(jsonStr, javaType);
	}

	/**
	 * 将对象转换成json
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object obj) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
}
