package quick.jedis.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import quick.jedis.config.ConfigManager;
import quick.jedis.impl.ICache;
import quick.jedis.impl.IRedis;

public class CacheFactory {
	// private static Object obj = new Object();
	private static Lock lock = new ReentrantLock();
	static {
		try {
			ConfigManager.InitCacheConfig();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static ICache GetCache(String cacheName)
	// {
	// String key = cacheName.ToLower();
	// Lock lock = CacheFactory.lock;
	// Boolean lockTaken = false;
	// try
	// {
	// lockTaken=lock.tryLock();
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(key))
	// return ConfigManager.CurrentConfig.CachePool[key];
	// }
	// finally
	// {
	// if (lockTaken)
	// lock.unlock();
	// }
	// InnerLogger.Warn("GetCache '" + key + "' 失败, 不存在该缓存对象");
	// Unity.CreateException("GetCache '" + key + "' 失败, 不存在该缓存对象");
	// return (ICache) null;
	// }

	public static IRedis CreateRedis(String cacheName, String writeServer, String readServer,
			Boolean isConvertSameCache) {
		IRedis redis1 = (IRedis) null;
		String index = cacheName.toLowerCase();
		if (!isConvertSameCache) {
			Lock lock = CacheFactory.lock;
			Boolean lockTaken = false;
			try {
				lockTaken = lock.tryLock();
				if (ConfigManager.CurrentConfig.CachePool.containsKey(index))
					redis1 = (IRedis) ConfigManager.CurrentConfig.CachePool.get(index);
			} finally {
				if (lockTaken)
					lock.unlock();
			}
			if (redis1 != null)
				return redis1;
		}
		IRedis redis2 = (IRedis) new RedisCache(index, writeServer, readServer, "0");
		if (ConfigManager.CurrentConfig.CachePool.containsKey(index))
			ConfigManager.CurrentConfig.CachePool.remove(index);
		ConfigManager.CurrentConfig.CachePool.put(index, (ICache) redis2);
		return redis2;
	}

	public static IRedis CreateRedis(String cacheName, String writeServer, String readServer, int defaultDb, Boolean isConvertSameCache)
  {
    IRedis redis1 = (IRedis) null;
    String index = cacheName.toLowerCase();
    if (!isConvertSameCache)
    {
      Lock lock = CacheFactory.lock;
      Boolean lockTaken = false;
      try
      {
        lockTaken=lock.tryLock();
        if (ConfigManager.CurrentConfig.CachePool.containsKey(index))
          redis1 = (IRedis) ConfigManager.CurrentConfig.CachePool.get(index);
      }
      finally
      {
        if (lockTaken)
          lock.unlock();
      }
      if (redis1 != null)
        return redis1;
    }
    IRedis redis2 = (IRedis) new RedisCache(index, writeServer, readServer, String.valueOf(defaultDb));
    if (ConfigManager.CurrentConfig.CachePool.containsKey(index))
      ConfigManager.CurrentConfig.CachePool.remove(index);
    ConfigManager.CurrentConfig.CachePool.put(index, (ICache) redis2);
    return redis2;
  }

	public static IRedis GetRedis(String cacheName) throws Exception {
		String key = cacheName.toLowerCase();
		Lock lock = CacheFactory.lock;
		Boolean lockTaken = false;
		try {
			lockTaken = lock.tryLock();
			if (ConfigManager.CurrentConfig.CachePool.containsKey(key))
				return (IRedis) ConfigManager.CurrentConfig.CachePool.get(key);
		} finally {
			if (lockTaken)
				lock.unlock();
		}
		InnerLogger.Warn("GetRedis '" + key + "' 失败, 不存在该缓存对象");
		Unity.CreateException("GetRedis '" + key + "' 失败, 不存在该缓存对象");
		return (IRedis) null;
	}

	// public static IMemcache CreateMemcache(String cacheName, String
	// serverIPs, String encoding, Boolean isConvertSameCache = true)
	// {
	// IMemcache memcache1 = (IMemcache) null;
	// String index = cacheName.ToLower();
	// if (!isConvertSameCache)
	// {
	// Lock lock = CacheFactory.lock;
	// Boolean lockTaken = false;
	// try
	// {
	// lockTaken=lock.tryLock();
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(index))
	// memcache1 = (IMemcache) ConfigManager.CurrentConfig.CachePool[index];
	// }
	// finally
	// {
	// if (lockTaken)
	// lock.unlock();
	// }
	// if (memcache1 != null)
	// return memcache1;
	// }
	// IMemcache memcache2 = (IMemcache) new MemcacheCache(index, serverIPs,
	// "Text", "0", "0", "0", "0", encoding);
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(index))
	// ConfigManager.CurrentConfig.CachePool.Remove(index);
	// ConfigManager.CurrentConfig.CachePool.Add(index, (ICache) memcache2);
	// return memcache2;
	// }

	// public static IMemcache CreateMemcache(String cacheName, String
	// serverIPs, String protocol, String connectionTimeout, String
	// socketTimeout, String minConnections, String maxConnections, String
	// encoding, Boolean isConvertSameCache = true)
	// {
	// IMemcache memcache1 = (IMemcache) null;
	// String index = cacheName.ToLower();
	// if (!isConvertSameCache)
	// {
	// Lock lock = CacheFactory.lock;
	// Boolean lockTaken = false;
	// try
	// {
	// lockTaken=lock.tryLock();
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(index))
	// memcache1 = (IMemcache) ConfigManager.CurrentConfig.CachePool[index];
	// }
	// finally
	// {
	// if (lockTaken)
	// lock.unlock();
	// }
	// if (memcache1 != null)
	// return memcache1;
	// }
	// IMemcache memcache2 = (IMemcache) new MemcacheCache(index, serverIPs,
	// protocol, connectionTimeout, socketTimeout, minConnections,
	// maxConnections, encoding);
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(index))
	// ConfigManager.CurrentConfig.CachePool.Remove(index);
	// ConfigManager.CurrentConfig.CachePool.Add(index, (ICache) memcache2);
	// return memcache2;
	// }

	// public static IMemcache GetMemcache(String cacheName)
	// {
	// String key = cacheName.ToLower();
	// Lock lock = CacheFactory.lock;
	// Boolean lockTaken = false;
	// try
	// {
	// lockTaken=lock.tryLock();
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(key))
	// return (IMemcache) ConfigManager.CurrentConfig.CachePool[key];
	// }
	// finally
	// {
	// if (lockTaken)
	// lock.unlock();
	// }
	//
	// Unity.CreateException("GetMemcache '" + key + "' 失败, 不存在该缓存对象");
	// return (IMemcache) null;
	// }

	// public static IRunTime GetRunTime(String cacheName) {
	// String key = cacheName.ToLower();
	// Lock lock = CacheFactory.lock;
	// Boolean lockTaken = false;
	// try {
	// lockTaken = lock.tryLock();
	// if (ConfigManager.CurrentConfig.CachePool.ContainsKey(key))
	// return (IRunTime) ConfigManager.CurrentConfig.CachePool[key];
	// } finally {
	// if (lockTaken)
	// lock.unlock();
	// }
	// InnerLogger.Warn("GetRunTime '" + key + "' 失败, 不存在该缓存对象");
	// Unity.CreateException("GetRunTime '" + key + "' 失败, 不存在该缓存对象");
	// return (IRunTime) null;
	// }
}
