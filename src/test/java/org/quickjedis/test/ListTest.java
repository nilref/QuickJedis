package org.quickjedis.test;

import junit.framework.Assert;
import org.junit.Test;
import org.quickjedis.utils.JsonHelper;

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
    public void testRPop() {
        System.out.println("testRPop");

        int num1 = RedisDefined.TestRedis.RPop("list-test-1", Integer.class);
        Assert.assertEquals(1, num1);
        int num2 = RedisDefined.TestRedis.RPop("list-test-1", Integer.class);
        Assert.assertEquals(2, num2);

    }
}
