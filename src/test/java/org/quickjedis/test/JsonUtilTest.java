package org.quickjedis.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.quickjedis.utils.JsonUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonUtilTest extends TestCase {

    private static UserInfo userInfo;

    public JsonUtilTest() {
        System.out.println("JsonHelperTest");
    }

    public JsonUtilTest(String name) {
        super(name);
        System.out.println("JsonHelperTest(name)");
        userInfo = new UserInfo("mahatma", 18, true);
    }

    public void testString() {
        String val2 = JsonUtil.toJson(new BigDecimal("123456789123111111111111111111111111111111111111112.123456789123111111111111111111111111111111111111112"));
        BigDecimal num2 = JsonUtil.toObject(val2, BigDecimal.class);
        Assert.assertEquals("123456789123111111111111111111111111111111111111112.123456789123111111111111111111111111111111111111112", val2);
        String val = JsonUtil.toJson(new Double("1.234567891234567"));
        Double num = JsonUtil.toObject(val, Double.class);
        Assert.assertEquals("1.234567891234567", val);
        String val1 = JsonUtil.toJson(new Float("1.2345678"));
        Float num1 = JsonUtil.toObject(val1, Float.class);
        Assert.assertEquals("1.2345678", val1);
    }


    public void testArray() {
        System.out.println("testArray");
        UserInfo[] userArray = new UserInfo[]{userInfo, userInfo};
        String userArrayStr = JsonUtil.toJson(userArray);
        Assert.assertNotNull(userArrayStr);
        System.out.println(userArrayStr);

        UserInfo[] userArrayTmp = JsonUtil.toObject(userArrayStr, UserInfo[].class);
        Assert.assertNotNull(userArrayTmp);
        Assert.assertEquals(userArray.length, userArrayTmp.length);
    }

    public void testArrayList() {
        System.out.println("testArrayList");
        List<UserInfo> userLst = new ArrayList<UserInfo>();
        userLst.add(userInfo);
        userLst.add(userInfo);
        String userLstStr = JsonUtil.toJson(userLst);
        Assert.assertNotNull(userLstStr);
        System.out.println(userLstStr);

        @SuppressWarnings("unchecked")
        List<UserInfo> userLstTmp = JsonUtil.toObject(userLstStr, List.class);
        Assert.assertNotNull(userLstTmp);
        Assert.assertEquals(userLst.size(), userLstTmp.size());
    }

    public void testHashMap() {
        System.out.println("testHashMap");
        HashMap<String, UserInfo> userMap = new HashMap<String, UserInfo>();
        userMap.put("user1", userInfo);
        userMap.put("user2", userInfo);
        userMap.put("user3", userInfo);
        String mapStr = JsonUtil.toJson(userMap);
        Assert.assertNotNull(mapStr);
        System.out.println(mapStr);

        HashMap<String, UserInfo> userMapTmp = JsonUtil.toObject(mapStr, HashMap.class);
        Assert.assertNotNull(userMapTmp);
        Assert.assertEquals(userMapTmp.size(), 3);
    }

    public void testDateTime() {
        String json = JsonUtil.toJson(userInfo);
        Assert.assertNotNull(json);
        UserInfo userInfo1 = JsonUtil.toObject(json, UserInfo.class);
        Assert.assertNotNull(userInfo1);
    }
}
