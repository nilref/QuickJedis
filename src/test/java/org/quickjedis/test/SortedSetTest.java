package org.quickjedis.test;

import org.junit.Test;

public class SortedSetTest extends MyTest {

    @Test
    public void testString() {
        System.out.println("testString");

        double score = RedisDefined.TestRedis.ZScore("test", "test");
    }

    @Test
    public void testObject() {
        System.out.println("testObject");


    }

}
