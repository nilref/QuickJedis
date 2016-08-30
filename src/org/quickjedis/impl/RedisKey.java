package org.quickjedis.impl;

public interface RedisKey {
	/**
	 * 设置 key 的过期时间
	 * 
	 * @param key
	 *            缓存的 key
	 * @param seconds
	 *            过期时间，单位：秒
	 * @return
	 */
	boolean Expire(String key, int seconds);

	/**
	 * 查看 key 的到期时间
	 * 
	 * @param key
	 *            缓存的 key
	 * @return
	 */
	long TTL(String key);

	/**
	 * 删除 key 以及对应的内容
	 * 
	 * @param key
	 *            缓存的 key
	 * @return
	 */
	long Del(String key);
	// DUMP
	// EXISTS
	// EXPIREAT
	// KEYS
	// MIGRATE
	// MOVE
	// OBJECT
	// PERSIST
	// PEXPIRE
	// PEXPIREAT
	// PTTL
	// RANDOMKEY
	// RENAME
	// RENAMENX
	// RESTORE
	// SORT
	// TYPE
	// SCAN
}
