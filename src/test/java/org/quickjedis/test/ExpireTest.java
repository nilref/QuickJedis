package org.quickjedis.test;

import org.junit.Test;

import junit.framework.Assert;

public class ExpireTest extends MyTest {

	public ExpireTest(String name) {
		super(name);
		System.out.println("ExpireTest");
	}

	public ExpireTest() {
	}

	public void testAnnotation() {

	}

	@Test
	public void testExpire() throws InterruptedException {
		System.out.println("testExpire start");

		boolean res = RedisDefined.TestRedis.Set(key, userInfo);
		Assert.assertTrue(res);

		boolean expireRes = RedisDefined.TestRedis.Expire(key, 3);
		Assert.assertTrue(expireRes);

		Thread.sleep(2 * 1000);
		UserInfo userInfoTmp = RedisDefined.TestRedis.Get(key, UserInfo.class);
		Assert.assertNotNull(userInfoTmp);
		Assert.assertEquals(userInfoTmp.getAge(), 18);

		boolean res1 = RedisDefined.TestRedis.Set(key, userInfo);
		Assert.assertTrue(res1);
		boolean expireRes1 = RedisDefined.TestRedis.Expire(key, 3);
		Assert.assertTrue(expireRes1);
		Thread.sleep(3500);
		UserInfo userInfoTmp1 = RedisDefined.TestRedis.Get(key, UserInfo.class);
		Assert.assertNull(userInfoTmp1);
		System.out.println("testExpire end");
	}
}
