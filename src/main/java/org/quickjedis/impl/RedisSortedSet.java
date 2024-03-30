package org.quickjedis.impl;

import java.util.HashMap;
import java.util.Set;

/**
 * @author nilptr
 */
public interface RedisSortedSet {

    /**
     * 向有序集合添加一个或多个成员，同时为每个成员指定一个分数
     * @param setid 集合id
     * @param member 对象
     * @param score 分数
     * @return 返回值
     */
    long ZAdd(final String setid, final String member, final double score);

    /**
     * 向有序集合添加一个或多个成员，同时为每个成员指定一个分数
     * @param setid 集合id
     * @param member 对象
     * @param score 分数
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long ZAdd(final String setid, final T member, final double score);

    /**
     * 获取有序集合中元素的数量
     *
     * @param setid 集合id
     * @return 返回值
     */
    long ZCard(final String setid);

    /**
     * 计算有序集合中指定分数区间的成员数量
     *
     * @param setid 集合id
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回值
     */
    long ZCount(final String setid, final double min, final double max);
    // ZINCRBY

    /**
     * 返回有序集 setid 中，指定区间内的成员(返回排序分数)
     * @param setid 集合id
     * @param start 开始的下标
     * @param stop 结束的下标
     * @return 返回值
     */
    HashMap<String, Double> ZRangeWithScores(final String setid, final long start, final long stop);

    /**
     * 返回有序集 setid 中，指定区间内的成员(返回排序分数)
     *
     * @param setid 集合id
     * @param start     集合开始的下标
     * @param stop      集合结束的下标
     * @param className 要转换的类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> HashMap<T, Double> ZRangeWithScores(final String setid, final long start, final long stop,
                                            final Class<T> className);

    /**
     * 返回有序集 setid 中，指定区间内的成员
     * @param setid 集合id
     * @param start 开始的下标
     * @param stop 结束的下标
     * @return 返回值
     */
    Set<String> ZRange(final String setid, final long start, final long stop);

    /**
     * 返回有序集 setid 中，指定区间内的成员
     *
     * @param setid     集合id
     * @param start     集合开始的下标
     * @param stop      集合结束的下标
     * @param className 需要转换的类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> Set<T> ZRange(final String setid, final long start, final long stop, final Class<T> className);

    // ZREM
    // ZREMRANGEBYRANK
    // ZREMRANGEBYSCORE


    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列 (返回排序分数)
     * @param setid 集合id
     * @param start 开始的下标
     * @param stop 结束的下标
     * @return 返回值
     */
    HashMap<String, Double> ZRevRangeWithScores(final String setid, final long start, final long stop);

    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列 (返回排序分数)
     * @param setid 集合id
     * @param start 开始的下标
     * @param stop 结束的下标
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> HashMap<T, Double> ZRevRangeWithScores(final String setid, final long start, final long stop,
                                               final Class<T> className);

    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列
     * @param setid 集合id
     * @param start 开始的下标
     * @param stop 结束的下标
     * @return 返回值
     */
    Set<String> ZRevRange(final String setid, final long start, final long stop);

    /**
     * 返回有序集中，指定区间内的成员，按分数值递减(从大到小)来排列
     * @param setid 集合id
     * @param start 开始的下标
     * @param stop 结束的下标
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> Set<T> ZRevRange(final String setid, final long start, final long stop, final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列 (返回排序分数)
     * @param setid 集合id
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回值
     */
    HashMap<String, Double> ZRangeByScoreWithScores(final String setid, final double min, final double max);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列 (返回排序分数)
     * @param setid 集合id
     * @param min 最小分数
     * @param max 最大分数
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> HashMap<T, Double> ZRangeByScoreWithScores(final String setid, final double min, final double max,
                                                   final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列
     * @param setid 集合id
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回值
     */
    Set<String> ZRangeByScore(final String setid, final double min, final double max);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递增(从小到大)次序排列
     * @param setid 集合id
     * @param min 最小分数
     * @param max 最大分数
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> Set<T> ZRangeByScore(final String setid, final double min, final double max, final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列 (返回排序分数)
     * @param setid 集合id
     * @param max 最大分数
     * @param min 最小分数
     * @return 返回值
     */
    HashMap<String, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列 (返回排序分数)
     * @param setid 集合id
     * @param max 最大分数
     * @param min 最小分数
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> HashMap<T, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min,
                                                      final Class<T> className);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列
     * @param setid 集合id
     * @param max 最大分数
     * @param min 最小分数
     * @return 返回值
     */
    Set<String> ZRevRangeByScore(final String setid, final double max, final double min);

    /**
     * 返回有序集合中指定分数区间的成员列表，有序集成员按分数值递减(从大到小)次序排列
     * @param setid 集合id
     * @param max 最大分数
     * @param min 最小分数
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> Set<T> ZRevRangeByScore(final String setid, final double max, final double min, final Class<T> className);

    /**
     * 返回有序集合中指定对象的score
     * @param setid 集合id
     * @param member 对象
     * @return 返回值
     */
     double ZScore(final String setid, final String member);

    /**
     * 返回有序集合中指定对象的score
     * @param setid 集合id
     * @param member 对象
     * @param <T> 泛型
     * @return 返回值
     */
     <T> double ZScore(final String setid, final T member);

    /**
     * 返回有序集中指定成员的排名
     * @param setid 集合id
     * @param member 对象
     * @return 返回值
     */
    long ZRank(final String setid, final String member);

    /**
     * 返回有序集中指定成员的排名
     * @param setid 集合id
     * @param member 对象
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long ZRank(final String setid, final T member);

    // ZREVRANK
    // ZUNIONSTORE
    // ZINTERSTORE
    // ZSCAN
}
