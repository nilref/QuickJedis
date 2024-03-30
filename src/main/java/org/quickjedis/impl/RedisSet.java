package org.quickjedis.impl;

import java.util.List;

public interface RedisSet {

    /**
     * 将一个或多个元素添加到集合中
     *
     * @param setid 集合id
     * @param member 对象
     * @return 返回值
     */
    long SAdd(final String setid, final String... member);

    /**
     * 将一个或多个元素添加到集合中
     *
     * @param setid 集合id
     * @param member 对象
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long SAdd(final String setid, final T... member);

    /**
     * 获取集合中元素的数量
     *
     * @param setid 集合id
     * @return 返回值
     */
    long SCard(final String setid);

    /**
     * 获取集合中的所有元素
     * @param setid 集合id
     * @return 返回值
     */
    List<String> SMembers(final String setid);

    /**
     * 获取集合中的所有元素
     *
     * @param setid 集合id
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> List<T> SMembers(final String setid, final Class<T> className);

    /**
     * 从集合中随机移除并返回一个或多个元素
     * @param setid 集合id
     * @return 返回值
     */
    String SPop(final String setid);

    /**
     * 从集合中随机移除并返回一个或多个元素
     *
     * @param setid 集合id
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> T SPop(final String setid, final Class<T> className);

    /**
     * 从集合中随机返回一个或多个元素，但并不会从集合中删除这些元素
     * @param setid 集合id
     * @param count 指定返回数量
     * @return 返回值
     */
    List<String> SRandMember(final String setid, int count);

    /**
     * 从集合中随机返回一个或多个元素，但并不会从集合中删除这些元素
     *
     * @param setid 集合id
     * @param count     （可选）要返回的元素数量。如果未指定，默认返回一个元素。
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
     */
    <T> List<T> SRandMember(final String setid, int count, final Class<T> className);

    // SREM
    // SUNION
    // SUNIONSTORE
    // SSCAN
    // SDIFF
    // SDIFFSTORE
    // SINTER
    // SINTERSTORE
    // SISMEMBER
    // SMOVE
}
