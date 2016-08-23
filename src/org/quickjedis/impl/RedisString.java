package org.quickjedis.impl;

import java.util.List;

public interface RedisString {
	/**
	 * 获得指定类型的对象list
	 * 
	 * @param key
	 *            缓存的 key
	 * @param className
	 *            要转换的对象类型 MyClass.class
	 * @return
	 */
	<T> List<T> GetList(String key, Class<T> className);

	/**
	 * 获取指定类型的对象
	 * 
	 * @param key
	 *            缓存的 key
	 * @param className
	 *            要转换的对象类型 MyClass.class
	 * @return
	 */
	<T> T Get(String key, Class<T> className);

	/**
	 * 获取字符串
	 * 
	 * @param key
	 *            缓存的 key
	 * @return
	 */
	String GetString(String key);

	/**
	 * 获取byte[]
	 * 
	 * @param key
	 *            缓存的 key
	 * @return
	 */
	byte[] GetBytes(String key);

	/**
	 * @param key
	 *            缓存的 key
	 * @param ListObject
	 *            要储存的对象 list
	 * @return
	 */
	<T> Boolean Set(String key, List<T> ListObject);

	/**
	 * @param key
	 *            缓存的 key
	 * @param ListObject
	 *            要储存的对象 list
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	<T> Boolean Set(String key, List<T> ListObject, int cacheMinutes);

	// Object
	/**
	 * @param key
	 *            缓存的 key
	 * @param targetObject
	 *            要储存的对象
	 * @return
	 */
	<T> Boolean Set(String key, T targetObject);

	/**
	 * @param key
	 *            缓存的 key
	 * @param targetObject
	 *            要储存的对象
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	<T> Boolean Set(String key, T targetObject, int cacheMinutes);

	// String
	/**
	 * @param key
	 *            缓存的 key
	 * @param text
	 *            需要保存的字符串
	 * @return
	 */
	Boolean Set(String key, String text);

	/**
	 * @param key
	 *            缓存的 key
	 * @param text
	 *            需要保存的字符串
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	Boolean Set(String key, String text, int cacheMinutes);

	// byte[]
	/**
	 * @param key
	 *            缓存的 key
	 * @param bytes
	 *            要储存的字节数组
	 * @return
	 */
	Boolean Set(String key, byte[] bytes);

	/**
	 * @param key
	 *            缓存的 key
	 * @param bytes
	 *            要储存的字节数组
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	Boolean Set(String key, byte[] bytes, int cacheMinutes);
}
