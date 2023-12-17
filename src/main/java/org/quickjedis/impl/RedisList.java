package org.quickjedis.impl;

public interface RedisList {
    // BLPOP
    // BRPOP
    // BRPOPLPUSH
    // LINDEX
    // LINSERT
    long LLen(final String queueId);
    // LPOP
    <T> long LPush(final String queueId, final T value);
    <T> long LPush(final String queueId, final T... value);
    <T> long RPush(final String queueId, final T value);
    <T> long RPush(final String queueId, final T... value);
    // LPUSHX
    // LRANGE
    // LREM
    // LSET
    // LTRIM
    <T> T RPop(final String queueId, final Class<T> className);
    // RPOPLPUSH
    // RPUSH
    // RPUSHX
}
