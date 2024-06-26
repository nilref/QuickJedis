package org.quickjedis.impl;

import java.util.List;

public interface RedisString {
	/**
	 * 获得指定类型的对象list
	 * 
	 * @param key 缓存的 key
	 * @param className 要转换的对象类型
	 * @param <T> 泛型
	 * @return 返回
	 */
	<T> List<T> GetList(final String key, final Class<T> className);

	/**
	 * 获取指定类型的对象
	 * @param key 缓存的 key
	 * @param className 要转换的对象类型
	 * @param <T> 泛型
	 * @return 返回
	 */
	<T> T Get(final String key, final Class<T> className);

	/**
	 * 获取字符串
	 * 
	 * @param key 缓存的 key
	 * @return 返回
	 */
	String GetString(final String key);

	/**
	 * 获取byte[]
	 * 
	 * @param key 缓存的 key
	 * @return 返回
	 */
	byte[] GetBytes(final String key);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @param <T> 泛型
	 * @return 返回
	 */
	<T> boolean Set(final String key, final List<T> value);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @param cacheMinutes 过期时间，单位：分钟
	 * @param <T> 泛型
	 * @return 返回
	 */
	<T> boolean Set(final String key, final List<T> value, final int cacheMinutes);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @param <T> 泛型
	 * @return 返回
	 */
	<T> boolean Set(final String key, final T value);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @param cacheMinutes 过期时间，单位：分钟
	 * @param <T> 泛型
	 * @return 返回
	 */
	<T> boolean Set(final String key, T value, final int cacheMinutes);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @return 返回
	 */
	boolean Set(final String key, final String value);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @param cacheMinutes 过期时间，单位：分钟
	 * @return 返回
	 */
	boolean Set(final String key, final String value, final int cacheMinutes);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @return 返回
	 */
	boolean Set(final String key, final byte[] value);

	/**
	 * @param key 缓存的 key
	 * @param value 要写入的值
	 * @param cacheMinutes 过期时间，单位：分钟
	 * @return 返回
	 */
	boolean Set(final String key, final byte[] value, final int cacheMinutes);

	/**
	 * @param key 自增的key
	 * @return 返回
	 */
	long Incr(final String key);

	// APPEND
	// BITCOUNT
	// BITOP
	// DECR
	// DECRBY
	// GETBIT
	// GETRANGE
	// GETSET
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
