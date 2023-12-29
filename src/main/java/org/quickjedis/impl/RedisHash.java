package org.quickjedis.impl;

public interface RedisHash {
    /**
     * 删除 HASH 表中的 field
     * @param hashId
     * @param field
     * @return
     */
    long HDel(final String hashId, final String field);

    /**
     * 判断key对应的HASH中是否存在field
     *
     * @param hashId
     * @param field
     * @return
     */
    Boolean HExists(final String hashId, final String field);

    /**
     * 获取 hash 表中对应字段的值
     *
     * @param hashId    缓存的 hashId
     * @param field     hash 的 字段名
     * @param className 要转换的类型
     * @return
     */
    <T> T HGet(final String hashId, final String field, final Class<T> className);

    /**
     * 获取 hash 表中对应字段的值
     *
     * @param hashId 缓存的 hashId
     * @param field  hash 的字段名
     * @return
     */
    String HGet(final String hashId, final String field);

    /**
     * 向 hash 表中写入值
     * @param key
     * @param field
     * @param value
     * @return
     */
    long HSet(final String key, final String field, final String value);

    /**
     * 向 hash 表中写入值
     *
     * @param hashId 缓存的 hashId
     * @param field  hash 的字段名
     * @param value  要写入的值
     * @return
     */
    <T> long HSet(final String hashId, final String field, final T value);

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment
     *
     * @param hashId    缓存的 hashId
     * @param field     hash 的字段名
     * @param increment 需要增加的数值
     * @return
     */
    long HIncrBy(final String hashId, final String field, final long increment);

    // HGETALL
    // HINCRBYFLOAT
    // HKEYS
    // HLEN
    // HMGET
    // HMSET
    // HSETNX
    // HVALS
    // HSCAN
}
