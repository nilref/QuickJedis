package org.quickjedis.core;

import java.util.Dictionary;
import java.util.List;

import org.quickjedis.impl.RedisHash;
import org.quickjedis.impl.RedisKey;
import org.quickjedis.impl.RedisList;
import org.quickjedis.impl.RedisSet;
import org.quickjedis.impl.RedisSortedSet;
import org.quickjedis.impl.RedisString;

import redis.clients.jedis.Tuple;

public abstract class Redis extends CacheBase
		implements RedisKey, RedisString, RedisHash, RedisList, RedisSet, RedisSortedSet {
	public Redis(String name) {
		super(name);
	}

	@Override
	public abstract <T> List<T> GetList(String key, Class<T> className);

	@Override
	public abstract <T> T Get(String key, Class<T> className);

	@Override
	public abstract String GetString(String key);

	@Override
	public abstract byte[] GetBytes(String key);

	@Override
	public abstract <T> boolean Set(String key, List<T> ListObject);

	@Override
	public abstract <T> boolean Set(String key, List<T> ListObject, int cacheMinutes);

	@Override
	public abstract <T> boolean Set(String key, T targetObject);

	@Override
	public abstract <T> boolean Set(String key, T targetObject, int cacheMinutes);

	@Override
	public abstract boolean Set(String key, String text);

	@Override
	public abstract boolean Set(String key, String text, int cacheMinutes);

	@Override
	public abstract boolean Set(String key, byte[] bytes);

	@Override
	public abstract boolean Set(String key, byte[] bytes, int cacheMinutes);

	@Override
	public abstract boolean Expire(String key, int seconds);

	@Override
	public abstract long TTL(String key);

	@Override
	public abstract long Del(String key);

	@Override
	public abstract <T> T Hget(String key, String field, Class<T> className);

	@Override
	public abstract byte[] HgetBytes(String key, String field);

	@Override
	public abstract boolean Hset(String key, String field, byte[] value);

	@Override
	public abstract boolean Hset(String key, String field, String value);

	@Override
	public abstract long HincrBy(String key, String field, long increment);

	abstract boolean Exists(String key);

	// T Get<T>(String key, Func<T> initItemFunc, int cacheMinutes = 0);

	abstract long LLen(String queueId);

	// long Push<T>(String queueId, T value);

	abstract long Push(String queueId, String value);

	// T Pop<T>(String queueId);

	abstract String Pop(String queueId);

	abstract String LIndex(String listId, int listIndex);

	abstract boolean Remove(String key);

	abstract long SCARD(String setid);

	abstract long SADD(String setid, String member);

	abstract long SREM(String setid, String member);

	abstract String SPOP(String setid);

	abstract List<String> SMEMBERS(String setid);

	abstract long SISMEMBER(String setid, String member);

	abstract void SMOVE(String setid, String toSetid, String member);

	abstract long ZCARD(String setid);

	abstract long ZADD(String setid, String member, int score);

	abstract long ZREM(String setid, String member);

	abstract double ZSCORE(String setid, String member);

	abstract double ZINCRBY(String setid, String member, int increment);

	abstract List<Tuple> ZRANGE(String setid, int start, int stop, boolean withScore);

	abstract List<Tuple> ZREVRANGE(String setid, int start, int stop, boolean withScore);

	abstract List<Tuple> ZRANGEBYSCORE(String setid, double min, double max, int skip, int take, boolean withScore);

	abstract long ZREMRANGEBYRANK(String setid, int min, int max);

	abstract long HIncrby(String hashId, String field, int incrementBy);

	abstract long HSet(String hashId, String field, String value);

	abstract void HMSet(String hashId, List<String> keyList, List<String> valueList);

	abstract List<String> HMGet(String hashId, List<String> keyList);

	abstract String HGet(String hashId, String field);

	abstract List<String> HGetValues(String hashId, List<String> keys);

	abstract Dictionary<String, String> HGetAll(String hashId);

	abstract long HDel(String hashId, String field);

	abstract long HLen(String hashId);

	abstract long Incr(String key);

	abstract long Decr(String key);

	abstract long Increment(String key, int amount);

	abstract long Decrement(String key, int amount);

	abstract long IncrementValueInHash(String hashId, String key, int incrementBy);

	abstract Dictionary<String, String> Info();

	abstract boolean Ping();

	abstract List<String> SearchKeys(String pattern);
}
