package org.quickjedis.test;

import org.quickjedis.core.CacheFactory;
import org.quickjedis.impl.IRedis;

public class RedisDefined {
	public static IRedis TestRedis;
	static {
		TestRedis = CacheFactory.GetRedis("TestRedis");
	}
}
