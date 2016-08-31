package org.quickjedis.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.quickjedis.utils.JsonHelper;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonHelperTest extends TestCase {

	private static UserInfo userInfo;

	public JsonHelperTest() {
		System.out.println("JsonHelperTest");
	}

	public JsonHelperTest(String name) {
		super(name);
		System.out.println("JsonHelperTest(name)");
		userInfo = new UserInfo("mahatma", 18, true);
	}

	public void testArray() {
		System.out.println("testArray");
		UserInfo[] userArray = new UserInfo[] { userInfo, userInfo };
		String userArrayStr = JsonHelper.toJson(userArray);
		Assert.assertNotNull(userArrayStr);
		System.out.println(userArrayStr);

		UserInfo[] userArrayTmp = JsonHelper.toObject(userArrayStr, UserInfo[].class);
		Assert.assertNotNull(userArrayTmp);
		Assert.assertEquals(userArray.length, userArrayTmp.length);
	}

	public void testArrayList() {
		System.out.println("testArrayList");
		List<UserInfo> userLst = new ArrayList<UserInfo>();
		userLst.add(userInfo);
		userLst.add(userInfo);
		String userLstStr = JsonHelper.toJson(userLst);
		Assert.assertNotNull(userLstStr);
		System.out.println(userLstStr);

		@SuppressWarnings("unchecked")
		List<UserInfo> userLstTmp = JsonHelper.toObject(userLstStr, List.class);
		Assert.assertNotNull(userLstTmp);
		Assert.assertEquals(userLst.size(), userLstTmp.size());
	}

	public void testHashMap() {
		System.out.println("testHashMap");
		HashMap<String, UserInfo> userMap = new HashMap<String, UserInfo>();
		userMap.put("user1", userInfo);
		userMap.put("user2", userInfo);
		userMap.put("user3", userInfo);
		String mapStr = JsonHelper.toJson(userMap);
		Assert.assertNotNull(mapStr);
		System.out.println(mapStr);

		HashMap<String, UserInfo> userMapTmp = JsonHelper.toObject(mapStr, HashMap.class);
		Assert.assertNotNull(userMapTmp);
		Assert.assertEquals(userMapTmp.size(), 3);
	}
}
