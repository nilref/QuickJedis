package org.quickjedis.impl;

import java.util.HashMap;
import java.util.Set;

import redis.clients.jedis.Tuple;

/**
 * @author wangsiyuan0728
 *
 */
public interface RedisSortedSet {
	// ZADD
	// ZCARD
	// ZCOUNT
	// ZINCRBY
	/**
	 * 返回有序集 key 中，指定区间内的成员(返回排序分数)
	 * 
	 * @param key
	 *            缓存的 key
	 * @param start
	 *            集合开始的下标
	 * @param stop
	 *            集合结束的下标
	 * @param className
	 *            要转换的类型
	 * @return
	 */
	<T> HashMap<Double, T> ZrangeWithScores(final String key, final long start, final long stop,
			final Class<T> className);

	/**
	 * 返回有序集 key 中，指定区间内的成员(返回排序分数)
	 * 
	 * @param key
	 *            缓存的 key
	 * @param start
	 *            集合开始的下标
	 * @param stop
	 *            集合结束的下标
	 * @return
	 */
	Set<Tuple> ZrangeWithScores(final String key, final long start, final long stop);

	/**
	 * 返回有序集 key 中，指定区间内的成员
	 * 
	 * @param key
	 *            缓存的 key
	 * @param start
	 *            集合开始的下标
	 * @param stop
	 *            集合结束的下标
	 * @param className
	 *            需要转换的类型
	 * @return
	 */
	<T> Set<T> Zrange(final String key, final long start, final long stop, final Class<T> className);

	/**
	 * 返回有序集 key 中，指定区间内的成员
	 * 
	 * @param key
	 *            缓存的 key
	 * @param start
	 *            集合开始的下标
	 * @param stop
	 *            集合结束的下标
	 * @return
	 */
	Set<byte[]> Zrange(final String key, final long start, final long stop);
	// ZRANGEBYSCORE
	// ZRANK
	// ZREM
	// ZREMRANGEBYRANK
	// ZREMRANGEBYSCORE
	// ZREVRANGE
	// ZREVRANGEBYSCORE
	// ZREVRANK
	// ZSCORE
	// ZUNIONSTORE
	// ZINTERSTORE
	// ZSCAN
}
