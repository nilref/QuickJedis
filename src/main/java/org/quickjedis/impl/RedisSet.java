package org.quickjedis.impl;

import java.util.List;

public interface RedisSet {

    <T> long SADD(final String setid, final T... member);

    long SCARD(final String setid);

    // SDIFF
    // SDIFFSTORE
    // SINTER
    // SINTERSTORE
    // SISMEMBER
    <T> List<T> SMEMBERS(final String setid, final Class<T> className);
    // SMOVE
    <T> T SPOP(final String setid, final Class<T> className);
    <T> List<T> SRANDMEMBER(final String setid, int count, final Class<T> className);
    // SREM
    // SUNION
    // SUNIONSTORE
    // SSCAN
}
