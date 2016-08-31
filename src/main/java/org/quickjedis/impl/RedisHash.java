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
	<T> T Hget(final String key, final String field, final Class<T> className);

	/**
	 * 获取 hash 表中对应字段的值
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的字段名
	 * @return
	 */
	String HgetString(final String key, final String field);

	/**
	 * 获取 hash 表中对应字段的值
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的字段名
	 * @return
	 */
	byte[] HgetBytes(final String key, final String field);

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
	boolean Hset(final String key, final String field, final String value);

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
	boolean Hset(final String key, final String field, final byte[] value);

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment
	 * 
	 * @param key
	 *            缓存的 key
	 * @param field
	 *            hash 的字段名
	 * @param increment
	 *            需要增加的数值
	 * @return
	 */
	long HincrBy(final String key, final String field, final long increment);
	// HGETALL
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
