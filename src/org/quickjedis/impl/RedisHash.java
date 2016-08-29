package org.quickjedis.impl;

public interface RedisHash {
	// HDEL
	// HEXISTS
	/**
	 * 获取 hash 表中对应字段的值
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的 字段名
	 * @param className
	 *            要转换的类型
	 * @return
	 */
	<T> T Hget(String key, String field, Class<T> className);

	/**
	 * 获取 hash 表中对应字段的值
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的字段名
	 * @return
	 */
	byte[] HgetBytes(String key, String field);

	/**
	 * 向 hash 表中写入值
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的字段名
	 * @param value
	 *            要写入的值
	 * @return
	 */
	Boolean Hset(String key, String field, String value);

	/**
	 * 向 hash 表中写入值
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的字段名
	 * @param value
	 *            要写入的值
	 * @return
	 */
	Boolean Hset(String key, String field, byte[] value);
	// HGETALL
	// HINCRBY
	// HINCRBYFLOAT
	// HKEYS
	// HLEN
	// HMGET
	// HMSET
	// HSET
	// HSETNX
	// HVALS
	// HSCAN
}
