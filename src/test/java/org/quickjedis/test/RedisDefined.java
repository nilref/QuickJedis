package org.quickjedis.test;

import org.quickjedis.core.CacheFactory;
import org.quickjedis.core.Redis;

public class RedisDefined {
	public static Redis TestRedis;
	static {
		TestRedis = CacheFactory.GetRedis("TestRedis");
	}
}
