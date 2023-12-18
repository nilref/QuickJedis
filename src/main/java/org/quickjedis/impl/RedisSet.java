package org.quickjedis.impl;

import java.util.List;

public interface RedisSet {

    /**
     * 将一个或多个元素添加到集合中
     *
     * @param setid
     * @param member
     * @return
     */
    <T> long SAdd(final String setid, final T... member);

    /**
     * 获取集合中元素的数量
     *
     * @param setid
     * @return
     */
    long SCard(final String setid);

    // SDIFF
    // SDIFFSTORE
    // SINTER
    // SINTERSTORE
    // SISMEMBER

    /**
     * 获取集合中的所有元素
     * @param setid
     * @param className
     * @param <T>
     * @return
     */
    <T> List<T> SMembers(final String setid, final Class<T> className);
    // SMOVE

    /**
     * 从集合中随机移除并返回一个或多个元素
     * @param setid
     * @param className
     * @param <T>
     * @return
     */
    <T> T SPop(final String setid, final Class<T> className);

    /**
     * 从集合中随机返回一个或多个元素，但并不会从集合中删除这些元素
     *
     * @param setid
     * @param count     （可选）要返回的元素数量。如果未指定，默认返回一个元素。
     * @param className
     * @param <T>
     * @return
     */
    <T> List<T> SRandMember(final String setid, int count, final Class<T> className);
    // SREM
    // SUNION
    // SUNIONSTORE
    // SSCAN
}
