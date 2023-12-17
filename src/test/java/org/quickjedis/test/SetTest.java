package org.quickjedis.test;

import junit.framework.Assert;
import org.junit.Test;
import org.quickjedis.utils.JsonHelper;

import java.util.List;

public class SetTest extends MyTest {

    @Test
    public void testString() {
        System.out.println("testString");

        String key = "TEST.SETSTRING";

        String jsonStr = JsonHelper.toJson(userInfo);
        long res = RedisDefined.TestRedis.SADD(key, jsonStr);
        Assert.assertEquals(res, 1);

        long SCARD = RedisDefined.TestRedis.SCARD(key);
        Assert.assertEquals(SCARD, 1);

        long res1 = RedisDefined.TestRedis.SADD(key, jsonStr);
        Assert.assertEquals(res1, 0);

        userInfo.setAge(userInfo.getAge() + 1);
        String jsonStr1 = JsonHelper.toJson(userInfo);
        long res2 = RedisDefined.TestRedis.SADD(key, jsonStr1);
        Assert.assertEquals(res2, 1);

        List<String> list = RedisDefined.TestRedis.SMEMBERS(key, String.class);
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 2);

        List<String> list1 = RedisDefined.TestRedis.SRANDMEMBER(key, 1, String.class);
        Assert.assertNotNull(list1);
        Assert.assertEquals(list1.size(), 1);

        long SCARD1 = RedisDefined.TestRedis.SCARD(key);
        for (int i = 0; i < SCARD1; i++) {
            String ret = RedisDefined.TestRedis.SPOP(key, String.class);
            UserInfo userInfoTmp = JsonHelper.toObject(ret, UserInfo.class);
            System.out.println("age: " + userInfoTmp.getAge());
            Assert.assertNotNull(userInfoTmp);
        }

        long SCARD2 = RedisDefined.TestRedis.SCARD(key);
        Assert.assertEquals(SCARD2, 0);
    }

    @Test
    public void testObject() {
        System.out.println("testObject");

        String key = "TEST.SETOBJECT";

        long res = RedisDefined.TestRedis.SADD(key, userInfo);
        Assert.assertEquals(res, 1);

        long SCARD = RedisDefined.TestRedis.SCARD(key);
        Assert.assertEquals(SCARD, 1);

        long res1 = RedisDefined.TestRedis.SADD(key, userInfo);
        Assert.assertEquals(res1, 0);

        userInfo.setAge(userInfo.getAge() + 1);
        long res2 = RedisDefined.TestRedis.SADD(key, userInfo);
        Assert.assertEquals(res2, 1);

        List<UserInfo> list = RedisDefined.TestRedis.SMEMBERS(key, UserInfo.class);
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 2);

        List<UserInfo> list1 = RedisDefined.TestRedis.SRANDMEMBER(key, 1, UserInfo.class);
        Assert.assertNotNull(list1);
        Assert.assertEquals(list1.size(), 1);

        long SCARD1 = RedisDefined.TestRedis.SCARD(key);
        for (int i = 0; i < SCARD1; i++) {
            UserInfo userInfoTmp = RedisDefined.TestRedis.SPOP(key, UserInfo.class);
            System.out.println("age: " + userInfoTmp.getAge());
            Assert.assertNotNull(userInfoTmp);
        }

        long SCARD2 = RedisDefined.TestRedis.SCARD(key);
        Assert.assertEquals(SCARD2, 0);
    }

}
