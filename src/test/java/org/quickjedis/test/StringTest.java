package org.quickjedis.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.quickjedis.utils.ConvertHelper;
import org.quickjedis.utils.JsonUtil;

import junit.framework.Assert;

public class StringTest extends MyTest {

    public StringTest(String name) {
        super(name);
        System.out.println("SetGetTest(name)");
    }

    public StringTest() {
        super();
        System.out.println("SetGetTest");
    }

    @Test
    public void testString() {
        System.out.println("testString");

        String jsonStr = JsonUtil.toJson(userInfo);
        boolean res = RedisDefined.TestRedis.Set(key, jsonStr);
        Assert.assertTrue(res);

        String jsonStrTmp = RedisDefined.TestRedis.GetString(key);
        Assert.assertNotNull(jsonStrTmp);
        Assert.assertEquals(jsonStr, jsonStrTmp);

        boolean res1 = RedisDefined.TestRedis.Set(key, "testStr");
        Assert.assertTrue(res1);

        String jsonStrTmp1 = RedisDefined.TestRedis.GetString(key);
        Assert.assertNotNull(jsonStrTmp1);
        Assert.assertEquals("testStr", jsonStrTmp1);

        UserInfo userInfoTmp = JsonUtil.toObject(jsonStrTmp, UserInfo.class);
        Assert.assertNotNull(userInfoTmp);
        Assert.assertEquals(18, userInfoTmp.getAge());
    }

    @Test
    public void testBytes() throws UnsupportedEncodingException {
        System.out.println("testBytes");
        byte[] bytes = ConvertHelper.StringToBytes(JsonUtil.toJson(userInfo));
        boolean bl = RedisDefined.TestRedis.Set(key, bytes);
        Assert.assertTrue(bl);
        byte[] userInfoBytes = RedisDefined.TestRedis.GetBytes(key);
        Assert.assertNotNull(userInfoBytes);
        String userInfoStr = new String(userInfoBytes, "UTF-8");
        UserInfo userInfoTmp = JsonUtil.toObject(userInfoStr, UserInfo.class);
        Assert.assertNotNull(userInfoTmp);
        Assert.assertEquals(18, userInfoTmp.getAge());
    }

    @Test
    public void testObject() {
        System.out.println("testObject");
        boolean bl = RedisDefined.TestRedis.Set(key, userInfo);
        Assert.assertTrue(bl);
        UserInfo userInfo = RedisDefined.TestRedis.Get(key, UserInfo.class);
        Assert.assertNotNull(userInfo);
        Assert.assertEquals(18, userInfo.getAge());
    }

    @Test
    public void testList() {
        System.out.println("testList");

        List<UserInfo> userLst = new ArrayList<UserInfo>();
        userLst.add(userInfo);
        userLst.add(userInfo);
        boolean res = RedisDefined.TestRedis.Set(key, userLst);
        Assert.assertTrue(res);
        List<UserInfo> userLstTmp = RedisDefined.TestRedis.GetList(key, UserInfo.class);
        Assert.assertNotNull(userLstTmp);
        Assert.assertFalse(userLstTmp.isEmpty());
    }

    @Test
    public void testIncr() {
        long testkey = 0;
        RedisDefined.TestRedis.Set("testKey", 5);
        testkey = RedisDefined.TestRedis.Incr("testKey");
        Assert.assertEquals(6, testkey);
        RedisDefined.TestRedis.Del("testKey");
        testkey = RedisDefined.TestRedis.Incr("testKey");
        Assert.assertEquals(1, testkey);
    }

}
