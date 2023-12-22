package org.quickjedis.impl;

public interface RedisList {
    // BLPOP
    // BRPOP
    // BRPOPLPUSH
    // LINDEX
    // LINSERT

    /**
     * 返回LIST的元素数量
     * @param queueId
     * @return
     */
    long LLen(final String queueId);
    // LPOP

    /**
     * 将一个值插入到列表头部
     * @param queueId
     * @param value
     * @return
     */
    long LPush(final String queueId, final String value);

    /**
     * 将多个值插入到列表头部
     * @param queueId
     * @param value
     * @return
     */
    long LPush(final String queueId, final String... value);

    /**
     * 将一个值插入到列表尾部
     * @param queueId
     * @param value
     * @return
     */
    long RPush(final String queueId, final String value);

    /**
     * 将多个值插入到列表尾部
     * @param queueId
     * @param value
     * @return
     */
    long RPush(final String queueId, final String... value);

    /**
     * 从列表的尾部取出一个值
     * @param queueId
     * @return
     */
    String RPop(final String queueId);

    /**
     * 将一个值插入到列表头部
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    <T> long LPush(final String queueId, final T value);

    /**
     * 将多个值插入到列表头部
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    <T> long LPush(final String queueId, final T... value);

    /**
     * 将一个值插入到列表尾部
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    <T> long RPush(final String queueId, final T value);

    /**
     * 将多个值插入到列表尾部
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    <T> long RPush(final String queueId, final T... value);

    /**
     * 从列表的尾部取出一个值
     * @param queueId
     * @param className
     * @param <T>
     * @return
     */
    <T> T RPop(final String queueId, final Class<T> className);
    // LPUSHX
    // LRANGE
    // LREM
    // LSET
    // LTRIM
    // RPOPLPUSH
    // RPUSH
    // RPUSHX
}
