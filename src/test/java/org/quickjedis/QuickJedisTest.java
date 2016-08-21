package org.quickjedis;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.quickjedis.utils.JsonHelper;

public class QuickJedisTest extends TestCase {

	private static final String key = "wsy.test";

	public QuickJedisTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public QuickJedisTest() {
		// TODO Auto-generated constructor stub
	}

	public void testSet() {
		System.out.println("test set");
		UserInfo userInfo = new UserInfo();
		userInfo.setName("mahatma");
		userInfo.setAge(18);
		userInfo.setIsMale(true);
		String jsonStr = JsonHelper.toJson(userInfo);
		boolean res = RedisDefined.TestRedis.Set(key, jsonStr);
		Assert.assertTrue(res);
	}

	public void testGet() throws UnsupportedEncodingException {
		System.out.println("test get");
		byte[] userInfoBytes = RedisDefined.TestRedis.GetBytes(key);
		Assert.assertNotNull(userInfoBytes);
		String userInfoStr = new String(userInfoBytes, "UTF-8");

		UserInfo userInfo = RedisDefined.TestRedis.Get(key, UserInfo.class);
		Assert.assertNotNull(userInfo);
		Assert.assertEquals(18, userInfo.getAge());

	}

}
