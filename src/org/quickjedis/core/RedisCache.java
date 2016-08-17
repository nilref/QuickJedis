package org.quickjedis.core;

import java.util.Dictionary;
import java.util.List;

import org.quickjedis.impl.IRedis;
import org.quickjedis.utils.ConvertHelper;
import org.quickjedis.utils.ConvertHelper.TryParseResult;
import org.quickjedis.utils.StringHelper;
import org.w3c.dom.Node;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

public class RedisCache extends CacheBase implements IRedis {
	private JedisPool RedisClientPool;
	private String Encoding;
	private int DB;

	public RedisCache(Node xmlNode) {
		super(XmlHelper.GetNodeAttr(xmlNode, "name"));
		_redisCache(XmlHelper.GetNodeAttr(xmlNode, "server"), XmlHelper.GetNodeAttr(xmlNode, "db"),
				XmlHelper.GetNodeAttr(xmlNode, "encoding"));
	}

	public RedisCache(String name, String server, String db) {
		super(name);
		_redisCache(server, db, "UTF-8");
	}

	private void _redisCache(String server, String db, String encoding) {

		// 设置DB
		TryParseResult<Integer> tryRes = ConvertHelper.TryParseInt(db);
		if (!tryRes.getSuccess())
			DB = 0;
		else
			DB = tryRes.getObject();
		if (DB > 15 || DB < 0)
			DB = 0;

		// 设置编码
		if (StringHelper.IsNullOrEmpty(encoding))
			Encoding = "UTF-8";
		else
			Encoding = encoding;
		this.RedisClientPool = this.CreateManager(server);
	}

	private JedisPool CreateManager(String hosts) {

		JedisPoolConfig config = new JedisPoolConfig();
		// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取
		// 最大连接数
		config.setMaxTotal(200);
		// 最大空闲连接数
		config.setMaxIdle(5);
		// 获取连接时的最大等待毫秒数，如果超过等待时间，则直接抛出JedisConnectionException， 小于零:阻塞不确定的时间
		// 默认-1
		config.setMaxWaitMillis(1000 * 10);
		// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
		config.setTestOnBorrow(true);

		String[] hostNport = hosts.split(":");
		return new JedisPool(config, hostNport[0], Integer.parseInt(hostNport[1]));
	}

	private Jedis GetResource() {
		Jedis client = this.RedisClientPool.getResource();
		client.select(this.DB);
		return client;
	}

	private byte[] StringToBytes(String str) {
		return ConvertHelper.StringToBytes(str, this.Encoding);
	}

	private String BytesToString(byte[] bytes) {
		return ConvertHelper.BytesToString(bytes, this.Encoding);
	}

	private <T> byte[] ObjectToJsonBytes(T obj) throws Exception {
		return ConvertHelper.ObjectToJsonBytes(obj, this.Encoding);
	}

	private <T> T JsonBytesToObject(byte[] bytes, Class<T> className) throws Exception {
		return ConvertHelper.JsonBytesToObject(bytes, className, this.Encoding);
	}

	@Override
	public <T> Boolean Set(String key, T targetObject, int cacheMinutes) {
		return _set(key, targetObject, cacheMinutes);
	}

	@Override
	public <T> Boolean Set(String key, T targetObject) {
		return _set(key, targetObject, -1);
	}

	@SuppressWarnings("deprecation")
	private <T> Boolean _set(String key, T targetObject, int cacheMinutes) {

		Jedis redisClient = null;
		try {
			redisClient = this.GetResource();
			byte[] keyArray = this.StringToBytes(key);
			byte[] objJsonArray = this.ObjectToJsonBytes(targetObject);
			if (cacheMinutes <= 0)
				return RedisResult.OK == redisClient.set(keyArray, objJsonArray);
			else {
				return RedisResult.OK == redisClient.set(keyArray, objJsonArray)
						&& 1 == redisClient.expire(keyArray, cacheMinutes * 60);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.RedisClientPool.returnBrokenResource(redisClient);
		}
		return false;
	}

	// 获取List
	public <T> List<T> GetList(String key, Class<T> className) {
		return null;
	}

	// 获取Byte[]
	public String GetBytes(String key) {
		return null;
	}

	@Override
	public String GetString(String key) {
		return _get(key, String.class);
	}

	@Override
	public <T> T Get(String key, Class<T> className) {
		return _get(key, className);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private <T> T _get(String key, Class<T> className) {
		Jedis redisClient = null;
		try {
			redisClient = this.GetResource();
			byte[] bs = redisClient.get(this.StringToBytes(key));
			if (className == String.class)
				return (T) this.BytesToString(bs);
			else
				return this.JsonBytesToObject(bs, className);

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
