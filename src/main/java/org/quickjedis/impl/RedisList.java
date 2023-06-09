package org.quickjedis.impl;

public interface RedisList {
    // BLPOP
    // BRPOP
    // BRPOPLPUSH
    // LINDEX
    // LINSERT
    // LLEN
    // LPOP
    <T> long LPush(final String queueId, final T value);
    <T> long LPush(final String queueId, final T... value);
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
