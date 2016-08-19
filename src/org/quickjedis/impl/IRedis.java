package org.quickjedis.impl;

import java.util.Dictionary;
import java.util.List;

import redis.clients.jedis.Tuple;

public interface IRedis {

	// 获取List
	<T> List<T> GetList(String key, Class<T> className);

	// 获取Object
	<T> T Get(String key, Class<T> className);

	// 获取String
	String GetString(String key);

	// 获取byte[]
	byte[] GetBytes(String key);

	// Object
	Boolean Set(String key, Object targetObject);

	// String
	Boolean Set(String key, String text);

	// byte[]
	Boolean Set(String key, byte[] bytes);

	// <T> Boolean Set(String key, T targetObject, int cacheMinutes);

	Boolean Expire(String key, int seconds);

	long TTL(String key);

	List<String> GetValues(List<String> keys);

	// List<T> GetValues<T>(List<String> keys);

	Boolean Exists(String key);

	long Del(String key);

	// T Get<T>(String key, Func<T> initItemFunc, int cacheMinutes = 0);

	long LLen(String queueId);

	// long Push<T>(String queueId, T value);

	long Push(String queueId, String value);

	// T Pop<T>(String queueId);

	String Pop(String queueId);

	String LIndex(String listId, int listIndex);

	Boolean Remove(String key);

	long SCARD(String setid);

	long SADD(String setid, String member);

	long SREM(String setid, String member);

	String SPOP(String setid);

	List<String> SMEMBERS(String setid);

	long SISMEMBER(String setid, String member);

	void SMOVE(String setid, String toSetid, String member);

	long ZCARD(String setid);

	long ZADD(String setid, String member, int score);

	long ZREM(String setid, String member);

	double ZSCORE(String setid, String member);

	double ZINCRBY(String setid, String member, int increment);

	List<Tuple> ZRANGE(String setid, int start, int stop, Boolean withScore);

	List<Tuple> ZREVRANGE(String setid, int start, int stop, Boolean withScore);

	List<Tuple> ZRANGEBYSCORE(String setid, double min, double max, int skip, int take, Boolean withScore);

	long ZREMRANGEBYRANK(String setid, int min, int max);

	long HIncrby(String hashId, String field, int incrementBy);

	long HSet(String hashId, String field, String value);

	void HMSet(String hashId, List<String> keyList, List<String> valueList);

	List<String> HMGet(String hashId, List<String> keyList);

	String HGet(String hashId, String field);

	List<String> HGetValues(String hashId, List<String> keys);

	Dictionary<String, String> HGetAll(String hashId);

	long HDel(String hashId, String field);

	long HLen(String hashId);

	long Incr(String key);

	long Decr(String key);

	long Increment(String key, int amount);

	long Decrement(String key, int amount);

	long IncrementValueInHash(String hashId, String key, int incrementBy);

	Dictionary<String, String> Info();

	Boolean Ping();

	List<String> SearchKeys(String pattern);
}
