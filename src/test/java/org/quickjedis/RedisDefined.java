package org.quickjedis;

import org.quickjedis.core.CacheFactory;
import org.quickjedis.impl.Redis;

public class RedisDefined {
	public static Redis TestRedis;
	static {
		TestRedis = CacheFactory.GetRedis("TestRedis");
	}
}
