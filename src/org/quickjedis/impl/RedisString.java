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
	 * @param value
	 *            要写入的值
	 * @return
	 */
	<T> boolean Set(String key, List<T> value);

	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	<T> boolean Set(String key, List<T> value, int cacheMinutes);

	// Object
	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @return
	 */
	<T> boolean Set(String key, T value);

	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	<T> boolean Set(String key, T value, int cacheMinutes);

	// String
	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @return
	 */
	boolean Set(String key, String value);

	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	boolean Set(String key, String value, int cacheMinutes);

	// byte[]
	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @return
	 */
	boolean Set(String key, byte[] value);

	/**
	 * @param key
	 *            缓存的 key
	 * @param value
	 *            要写入的值
	 * @param cacheMinutes
	 *            过期时间，单位：分钟
	 * @return
	 */
	boolean Set(String key, byte[] value, int cacheMinutes);

	// APPEND
	// BITCOUNT
	// BITOP
	// DECR
	// DECRBY
	// GETBIT
	// GETRANGE
	// GETSET
	// INCR
	// INCRBY
	// INCRBYFLOAT
	// MGET
	// MSET
	// MSETNX
	// PSETEX
	// SETBIT
	// SETEX
	// SETNX
	// SETRANGE
	// STRLEN
}
