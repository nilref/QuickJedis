package org.quickjedis.test;

import junit.framework.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class HashTest extends MyTest {

    @Test
    public void testString() {
        System.out.println("testString");
        RedisDefined.TestRedis.HSet("Hashkey", "testfield1", "testval");
        String val1 = RedisDefined.TestRedis.HGet("Hashkey", "testfield1");
        Assert.assertEquals("testval", val1);
        RedisDefined.TestRedis.HSet("Hashkey", "testfield2", new String("123456789"));
        String val2 = RedisDefined.TestRedis.HGet("Hashkey", "testfield2");
        Assert.assertEquals("123456789", val2);

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
