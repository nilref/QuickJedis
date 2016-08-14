package quick.jedis.core;

import java.util.Dictionary;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;
import org.w3c.dom.Node;

import quick.jedis.impl.ICache;
import quick.jedis.impl.IRedis;
import quick.jedis.utils.ConvertHelper;
import quick.jedis.utils.ConvertHelper.TryParseResult;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

public class RedisCache extends CacheBase implements IRedis {
	private JedisPool RedisClientPool;

	public RedisCache(Node xmlNode) {
		super(XmlHelper.GetNodeAttr(xmlNode, "name"));
		_redisCache(XmlHelper.GetNodeAttr(xmlNode, "name"), XmlHelper.GetNodeAttr(xmlNode, "write-server"),
				XmlHelper.GetNodeAttr(xmlNode, "read-server"), XmlHelper.GetNodeAttr(xmlNode, "db"));
	}

	public RedisCache(String name, String writeServer, String readServer, String db) {
		super(name);
		_redisCache(name, writeServer, readServer, db);
	}

	private void _redisCache(String name, String writeServer, String readServer, String db) {
		// String[] readWriteHosts = writeServer.split(",");
		// String[] readOnlyHosts = readServer.split(",");
		TryParseResult<Integer> tryRes = ConvertHelper.TryParseInt(db);
		int result;
		if (!tryRes.getSuccess())
			result = 0;
		else
			result = tryRes.getObject();
		if (result > 15 || result < 0)
			result = 0;
		this.RedisClientPool = this.CreateManager(writeServer, readServer, result);
	}

	private JedisPool CreateManager(String readWriteHosts, String readOnlyHosts, int defaultDb) {

		JedisPoolConfig config = new JedisPoolConfig();
		// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取
		// 最大连接数
		config.setMaxTotal(50);
		// 最大空闲连接数
		config.setMaxIdle(5);
		// 获取连接时的最大等待毫秒数，如果超过等待时间，则直接抛出JedisConnectionException， 小于零:阻塞不确定的时间
		// 默认-1
		config.setMaxWaitMillis(1000 * 30);
		// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
		config.setTestOnBorrow(true);

		// return new PooledRedisClientManager((IEnumerable<String>) strArray1,
		// (IEnumerable<String>) strArray2, config);
		return new JedisPool(config, readWriteHosts, Integer.parseInt(readWriteHosts.split(":")[1]));
	}

//	@Override
//	public Boolean Set(String key, String targetObject, int cacheMinutes) {
//		Jedis redisClient = null;
//		try {
//			redisClient = this.RedisClientPool.getResource();
//			byte[] numArray = ConvertHelper.StringToBytes(targetObject);
//			byte[] keyArray = ConvertHelper.StringToBytes(key);
//			if (cacheMinutes <= 0)
//				return RedisResult.OK == redisClient.set(keyArray, numArray);
//			else {
//				return RedisResult.OK == redisClient.set(keyArray, numArray)
//						&& 1 == redisClient.expire(keyArray, cacheMinutes * 60);
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			this.RedisClientPool.returnBrokenResource(redisClient);
//		}
//		return false;
//	}

	@Override
	public <T> Boolean Set(String key, T targetObject, int cacheMinutes) {

		Jedis redisClient = null;
		try {
			redisClient = this.RedisClientPool.getResource();
			byte[] keyArray = ConvertHelper.StringToBytes(key);
			byte[] objArray = ConvertHelper.ObjectToBytes(targetObject);
			if (cacheMinutes <= 0)
				return RedisResult.OK == redisClient.set(keyArray, objArray);
			else {
				return RedisResult.OK == redisClient.set(keyArray, objArray)
						&& 1 == redisClient.expire(keyArray, cacheMinutes * 60);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.RedisClientPool.returnBrokenResource(redisClient);
		}
		return false;
	}

//	@Override
//	public String Get(String key) {
//		Jedis redisClient = null;
//		try {
//			redisClient = this.RedisClientPool.getResource();
//			return ConvertHelper.BytesToString(redisClient.get(ConvertHelper.StringToBytes(key)));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			this.RedisClientPool.returnBrokenResource(redisClient);
//		}
//		return "";
//	}

	@Override
	public <T> T Get(String key) {
		Jedis redisClient = null;
		try {
			redisClient = this.RedisClientPool.getResource();
			return (T) ConvertHelper.BytesToObject(redisClient.get(ConvertHelper.StringToBytes(key)));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.RedisClientPool.returnBrokenResource(redisClient);
		}
		return null;
	}

	@Override
	public Boolean Expire(String key, int seconds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long TTL(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> GetValues(List<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long Del(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long LLen(String queueId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long Push(String queueId, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String Pop(String queueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String LIndex(String listId, int listIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long SCARD(String setid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long SADD(String setid, String member) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long SREM(String setid, String member) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String SPOP(String setid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> SMEMBERS(String setid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long SISMEMBER(String setid, String member) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void SMOVE(String setid, String toSetid, String member) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long ZCARD(String setid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long ZADD(String setid, String member, int score) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long ZREM(String setid, String member) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double ZSCORE(String setid, String member) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double ZINCRBY(String setid, String member, int increment) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Tuple> ZRANGE(String setid, int start, int stop, Boolean withScore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple> ZREVRANGE(String setid, int start, int stop, Boolean withScore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple> ZRANGEBYSCORE(String setid, double min, double max, int skip, int take, Boolean withScore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long ZREMRANGEBYRANK(String setid, int min, int max) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long HIncrby(String hashId, String field, int incrementBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long HSet(String hashId, String field, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void HMSet(String hashId, List<String> keyList, List<String> valueList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> HMGet(String hashId, List<String> keyList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String HGet(String hashId, String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> HGetValues(String hashId, List<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dictionary<String, String> HGetAll(String hashId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long HDel(String hashId, String field) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long HLen(String hashId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long Incr(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long Decr(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long Increment(String key, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long Decrement(String key, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long IncrementValueInHash(String hashId, String key, int incrementBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Dictionary<String, String> Info() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean Ping() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> SearchKeys(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean Exists(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean Remove(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
