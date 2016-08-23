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
	Boolean Expire(String key, int seconds);

	/**
	 * 查看 key 的到期时间
	 * 
	 * @param key
	 *            缓存的 key
	 * @return
	 */
	long TTL(String key);
}
