package org.quickjedis.test;

import junit.framework.Assert;
import org.junit.Test;
import org.quickjedis.utils.JsonHelper;

import java.math.BigDecimal;
import java.util.List;

public class HashTest extends MyTest {

    @Test
    public void testString() {
        System.out.println("testString");


    }

    @Test
    public void testObject() {
        System.out.println("testObject");
        RedisDefined.TestRedis.HSet("hashtest", "valtest1",
                new BigDecimal("232137823213782321378232137823213782321378232137823213782321378.7533050011"));
        RedisDefined.TestRedis.HSet("hashtest", "valtest2", new BigDecimal("0.0000000099").toPlainString());

        BigDecimal ret = RedisDefined.TestRedis.HGet("hashtest", "valtest2", BigDecimal.class);
        Assert.assertEquals(new BigDecimal("0.0000000099"), ret);
    }

}
