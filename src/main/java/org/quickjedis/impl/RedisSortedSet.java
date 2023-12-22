package org.quickjedis.impl;

import java.util.HashMap;
import java.util.Set;

/**
 * @author nilptr
 */
public interface RedisSortedSet {

    /**
     * 向有序集合添加一个或多个成员，同时为每个成员指定一个分数
     * @param setid
     * @param member
     * @param score
     * @return
     */
    long ZAdd(final String setid, final String member, final double score);

    /**
     * 向有序集合添加一个或多个成员，同时为每个成员指定一个分数
     * @param setid
     * @param member
     * @param score
     * @param <T>
     * @return
     */
    <T> long ZAdd(final String setid, final T member, final double score);

    /**
     * 获取有序集合中元素的数量
     *
     * @param setid
     * @return
     */
    long ZCard(final String setid);

    /**
     * 计算有序集合中指定分数区间的成员数量
     *
     * @param setid
     * @param min
     * @param max
     * @return
     */
    long ZCount(final String setid, final double min, final double max);
    // ZCOUNT
    // ZINCRBY

    /**
     * 返回有序集 key 中，指定区间内的成员(返回排序分数)
     * @param key
     * @param start
     * @param stop
     * @return
     */
    HashMap<String, Double> ZRangeWithScores(final String key, final long start, final long stop);

    /**
     * 返回有序集 key 中，指定区间内的成员(返回排序分数)
     *
     * @param key       缓存的 key
     * @param start     集合开始的下标
     * @param stop      集合结束的下标
     * @param className 要转换的类型
     * @return
     */
    <T> HashMap<T, Double> ZRangeWithScores(final String key, final long start, final long stop,
                                            final Class<T> className);

    /**
     * 返回有序集 key 中，指定区间内的成员
     * @param key
     * @param start
     * @param stop
     * @return
     */
    Set<String> ZRange(final String key, final long start, final long stop);

    /**
     * 返回有序集 key 中，指定区间内的成员
     *
     * @param key       缓存的 key
     * @param start     集合开始的下标
     * @param stop      集合结束的下标
     * @param className 需要转换的类型
     * @return
     */
    <T> Set<T> ZRange(final String key, final long start, final long stop, final Class<T> className);

    // ZRANK
    // ZREM
    // ZREMRANGEBYRANK
    // ZREMRANGEBYSCORE


    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列 (返回排序分数)
     * @param setid
     * @param start
     * @param stop
     * @return
     */
    HashMap<String, Double> ZRevRangeWithScores(final String setid, final long start, final long stop);

    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列 (返回排序分数)
     * @param setid
     * @param start
     * @param stop
     * @param className
     * @param <T>
     * @return
     */
    <T> HashMap<T, Double> ZRevRangeWithScores(final String setid, final long start, final long stop,
                                               final Class<T> className);

    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列
     * @param setid
     * @param start
     * @param stop
     * @return
     */
    Set<String> ZRevRange(final String setid, final long start, final long stop);

    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列
     * @param setid
     * @param start
     * @param stop
     * @param className
     * @param <T>
     * @return
     */
    <T> Set<T> ZRevRange(final String setid, final long start, final long stop, final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列 (返回排序分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    HashMap<String, Double> ZRangeByScoreWithScores(final String key, final double min, final double max);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列 (返回排序分数)
     * @param key
     * @param min
     * @param max
     * @param className
     * @param <T>
     * @return
     */
    <T> HashMap<T, Double> ZRangeByScoreWithScores(final String key, final double min, final double max,
                                                   final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> ZRangeByScore(final String key, final double min, final double max);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列
     * @param key
     * @param min
     * @param max
     * @param className
     * @param <T>
     * @return
     */
    <T> Set<T> ZRangeByScore(final String key, final double min, final double max, final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列 (返回排序分数)
     * @param setid
     * @param max
     * @param min
     * @return
     */
    HashMap<String, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列 (返回排序分数)
     * @param setid
     * @param max
     * @param min
     * @param className
     * @param <T>
     * @return
     */
    <T> HashMap<T, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min,
                                                      final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列
     * @param setid
     * @param max
     * @param min
     * @return
     */
    Set<String> ZRevRangeByScore(final String setid, final double max, final double min);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列
     * @param setid
     * @param max
     * @param min
     * @param className
     * @param <T>
     * @return
     */
    <T> Set<T> ZRevRangeByScore(final String setid, final double max, final double min, final Class<T> className);

    // ZREVRANK
    // ZSCORE
    // ZUNIONSTORE
    // ZINTERSTORE
    // ZSCAN
}
