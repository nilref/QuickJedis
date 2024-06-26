package org.quickjedis.impl;

import java.util.List;

public interface RedisKey {
    /**
     * 设置 key 的过期时间
     *
     * @param key     缓存的 key
     * @param seconds 过期时间，单位：秒
     * @return 返回true或false
     */
    boolean Expire(final String key, final int seconds);

    /**
     * 查看 key 的到期时间
     *
     * @param key 缓存的 key
     * @return 返回剩余时间
     */
    long TTL(final String key);

    /**
     * 删除 key 以及对应的内容
     *
     * @param key 缓存的 key
     * @return 被删除 key 的数量
     */
    long Del(final String key);

    // DUMP
    // EXISTS
    // EXPIREAT

    /**
     * 查找所有符合给定模式 pattern 的 key
     * @param pattern 匹配字符串
     * @return 符合给定模式 pattern 的 key
     */
    List<String> Keys(final String pattern);
    // MIGRATE
    // MOVE
    // OBJECT
    // PERSIST
    // PEXPIRE
    // PEXPIREAT
    // PTTL
    // RANDOMKEY
    // RENAME
    // RENAMENX
    // RESTORE
    // SORT
    // TYPE
    // SCAN
}
