package org.quickjedis.impl;

public interface RedisList {

    /**
     * 返回LIST的元素数量
     * @param queueId 队列id
     * @return 返回值
     */
    long LLen(final String queueId);

    /**
     * 将一个值插入到列表头部
     * @param queueId 队列id
     * @param value 写入值
     * @return 返回值
     */
    long LPush(final String queueId, final String value);

    /**
     * 将多个值插入到列表头部
     * @param queueId 队列id
     * @param value 写入值
     * @return 返回值
     */
    long LPush(final String queueId, final String... value);

    /**
     * 将一个值插入到列表尾部
     * @param queueId 队列id
     * @param value 写入值
     * @return 返回值
     */
    long RPush(final String queueId, final String value);

    /**
     * 将多个值插入到列表尾部
     * @param queueId 队列id
     * @param value 写入值
     * @return 返回值
     */
    long RPush(final String queueId, final String... value);

    /**
     * 从列表的尾部取出一个值
     * @param queueId 队列id
     * @return 返回值
     */
    String RPop(final String queueId);

    /**
     * 将一个值插入到列表头部
     * @param queueId 队列id
     * @param value 写入值
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long LPush(final String queueId, final T value);

    /**
     * 将多个值插入到列表头部
     * @param queueId 队列id
     * @param value 写入值
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long LPush(final String queueId, final T... value);

    /**
     * 将一个值插入到列表尾部
     * @param queueId 队列id
     * @param value 写入值
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long RPush(final String queueId, final T value);

    /**
     * 将多个值插入到列表尾部
     * @param queueId 队列id
     * @param value 写入值
     * @param <T> 泛型
     * @return 返回值
     */
    <T> long RPush(final String queueId, final T... value);

    /**
     * 从列表的尾部取出一个值
     * @param queueId 队列id
     * @param className 对象类型
     * @param <T> 泛型
     * @return 返回值
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
    // BLPOP
    // BRPOP
    // BRPOPLPUSH
    // LINDEX
    // LINSERT
    // LPOP
}
