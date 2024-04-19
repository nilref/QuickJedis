package org.quickjedis.test;

import junit.framework.Assert;
import org.junit.Test;

public class ListTest extends MyTest {

    @Test
    public void testLPush() {
        System.out.println("testLPush");
        RedisDefined.TestRedis.Del("list-test-1");

        long ret = RedisDefined.TestRedis.LPush("list-test-1", 1);
        Assert.assertEquals(1, ret);

        ret = RedisDefined.TestRedis.LPush("list-test-1", 2);
        Assert.assertEquals(2, ret);

        ret = RedisDefined.TestRedis.LPush("list-test-1", new Integer[]{3, 4});
        Assert.assertEquals(4, ret);
    }

    @Test
    public void testRPush() {
        System.out.println("testRPush");
        RedisDefined.TestRedis.RPush("exchanges_tickers_v2_exchangeslist", "zyberswap");
        long ret = RedisDefined.TestRedis.RPush("list-test-1", 999);
        int num1 = RedisDefined.TestRedis.RPop("list-test-1", Integer.class);
        Assert.assertEquals(999, num1);
    }

    @Test
    public void testLLen() {
        System.out.println("testLLen");
        long ret = RedisDefined.TestRedis.LLen("list-test-1");
        Assert.assertEquals(4, ret);
    }

    @Test
    public void testRPop() {
        System.out.println("testRPop");

        int num1 = RedisDefined.TestRedis.RPop("list-test-1", Integer.class);
        Assert.assertEquals(1, num1);
        int num2 = RedisDefined.TestRedis.RPop("list-test-1", Integer.class);
        Assert.assertEquals(2, num2);
        String exchange_id = RedisDefined.TestRedis.RPop("exchanges_tickers_v2_exchangeslist", String.class);
        Assert.assertNotNull(exchange_id);
    }
}
