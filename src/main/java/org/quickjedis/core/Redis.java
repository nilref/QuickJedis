package org.quickjedis.core;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.quickjedis.impl.RedisHash;
import org.quickjedis.impl.RedisKey;
import org.quickjedis.impl.RedisList;
import org.quickjedis.impl.RedisSet;
import org.quickjedis.impl.RedisSortedSet;
import org.quickjedis.impl.RedisString;

import redis.clients.jedis.Tuple;

import javax.xml.bind.annotation.XmlValue;

public abstract class Redis extends CacheBase
        implements RedisKey, RedisString, RedisHash, RedisList, RedisSet, RedisSortedSet {
    public Redis(final String name) {
        super(name);
    }

    @Override
    public abstract <T> List<T> GetList(final String key, final Class<T> className);

    @Override
    public abstract <T> T Get(final String key, final Class<T> className);

    @Override
    public abstract String GetString(final String key);

    @Override
    public abstract byte[] GetBytes(final String key);

    @Override
    public abstract <T> boolean Set(final String key, final List<T> ListObject);

    @Override
    public abstract <T> boolean Set(final String key, final List<T> ListObject, final int cacheMinutes);

    @Override
    public abstract <T> boolean Set(final String key, final T targetObject);

    @Override
    public abstract <T> boolean Set(final String key, final T targetObject, final int cacheMinutes);

    @Override
    public abstract boolean Set(final String key, final String text);

    @Override
    public abstract boolean Set(final String key, final String text, final int cacheMinutes);

    @Override
    public abstract boolean Set(final String key, final byte[] bytes);

    @Override
    public abstract boolean Set(final String key, final byte[] bytes, final int cacheMinutes);

    @Override
    public abstract boolean Expire(final String key, final int seconds);

    @Override
    public abstract long TTL(final String key);

    @Override
    public abstract long Del(final String key);

    abstract List<String> HGetValues(final String hashId, final List<String> keys);

    abstract Dictionary<String, String> HGetAll(final String hashId);

    @Override
    public abstract <T> T HGet(final String hashId, final String field, final Class<T> className);

    @Override
    public abstract String HGet(final String hashId, final String field);

    @Override
    public abstract <T> long HSet(final String hashId, final String field, final T value);

    @Override
    public abstract long HIncrBy(final String key, final String field, final long increment);

    abstract void HMSet(final String hashId, final List<String> keyList, final List<String> valueList);

    abstract List<String> HMGet(final String hashId, final List<String> keyList);

    abstract long HDel(final String hashId, final String field);

    abstract long HLen(final String hashId);

    abstract boolean Exists(final String key);

    // T Get<T>(final String key, Func<T> initItemFunc, final int cacheMinutes =
    // 0);
    @Override
    public abstract long LLen(final String queueId);

    /**
     * 将一个值插入到列表头部
     *
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public abstract <T> long LPush(final String queueId, final T value);

    /**
     * 将多个值插入到列表头部
     *
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public abstract <T> long LPush(final String queueId, final T... value);

    /**
     * 将一个值插入到列表尾部
     *
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public abstract <T> long RPush(final String queueId, final T value);

    /**
     * 将多个值插入到列表尾部
     *
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public abstract <T> long RPush(final String queueId, final T... value);

    /**
     * 从列表的尾部取出一个值
     *
     * @param queueId
     * @param className
     * @param <T>
     * @return
     */
    @Override
    public abstract <T> T RPop(final String queueId, final Class<T> className);

    abstract String LIndex(final String listId, final int listIndex);

    abstract boolean Remove(final String key);



    @Override
    public abstract <T> long SAdd(final String setid, final T... member);

    abstract long SREM(final String setid, final String member);


    @Override
    public abstract long SCard(final String setid);

    @Override
    public abstract <T> T SPop(final String setid, final Class<T> className);

    @Override
    public abstract <T> List<T> SMembers(final String setid, final Class<T> className);

    @Override
    public abstract <T> List<T> SRandMember(final String setid, int count, final Class<T> className);

    abstract long SISMEMBER(final String setid, final String member);

    abstract void SMOVE(final String setid, final String toSetid, final String member);

    @Override
    public abstract long ZCard(final String setid);

    @Override
    public abstract long ZCount(final String setid, final double min, final double max);

    @Override
    public abstract <T> long ZAdd(final String setid, final T member, final double score);

    @Override
    public abstract <T> HashMap<T, Double> ZRangeByScoreWithScores(final String key, final double min, final double max,
                                                   final Class<T> className);

    @Override
    public abstract <T> Set<T> ZRangeByScore(final String key, final double min, final double max, final Class<T> className);

    @Override
    public abstract <T> HashMap<T, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min,
                                                      final Class<T> className);

    @Override
    public abstract <T> Set<T> ZRevRangeByScore(final String setid, final double max, final double min, final Class<T> className);

    abstract long ZREM(final String setid, final String member);

    abstract double ZSCORE(final String setid, final String member);

    abstract double ZINCRBY(final String setid, final String member, final int increment);

    @Override
    public abstract <T> Set<T> ZRange(final String key, final long start, final long stop, final Class<T> className);

    @Override
    public abstract <T> HashMap<T, Double> ZRangeWithScores(final String key, final long start, final long stop,
                                                            final Class<T> className);

    @Override
    public abstract <T> HashMap<T, Double> ZRevRangeWithScores(final String setid, final long start, final long stop,
                                                               final Class<T> className);

    @Override
    public abstract <T> Set<T> ZRevRange(final String setid, final long start, final long stop, final Class<T> className);

    abstract long ZREMRANGEBYRANK(final String setid, final int min, final int max);

    @Override
    public abstract long Incr(final String key);

    abstract long Decr(final String key);

    abstract long Increment(final String key, final int amount);

    abstract long Decrement(final String key, final int amount);

    abstract long IncrementValueInHash(final String hashId, final String key, final int incrementBy);

    abstract Dictionary<String, String> Info();

    abstract boolean Ping();

    abstract List<String> SearchKeys(final String pattern);
}
