package org.quickjedis.core;

import java.util.Dictionary;
import java.util.List;

import org.quickjedis.model.RedisResult;
import org.quickjedis.utils.ConvertHelper;
import org.quickjedis.utils.ConvertHelper.TryParseResult;
import org.quickjedis.utils.JsonHelper;
import org.quickjedis.utils.StringHelper;
import org.w3c.dom.Node;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Tuple;

public class RedisCache extends Redis {
	private JedisPool RedisClientPool;
	private String Encoding;
	private int DataBase;

	public RedisCache(Node xmlNode) {
		this(XmlHelper.GetNodeAttr(xmlNode, "name"), XmlHelper.GetNodeAttr(xmlNode, "server"),
				XmlHelper.GetNodeAttr(xmlNode, "db"), XmlHelper.GetNodeAttr(xmlNode, "encoding"));
	}

	public RedisCache(String name, String server, String db) {
		this(name, server, db, "UTF-8");
	}

	public RedisCache(String name, String server, String db, String encoding) {
		super(name);
		// 设置DB
		TryParseResult<Integer> tryRes = ConvertHelper.TryParseInt(db);
		if (!tryRes.getSuccess())
			this.DataBase = 0;
		else
			this.DataBase = tryRes.getObject();
		if (this.DataBase > 15 || this.DataBase < 0)
			this.DataBase = 0;

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
		// return new JedisPool(config, hostNport[0],
		// Integer.parseInt(hostNport[1]));
		return new JedisPool(config, hostNport[0], Integer.parseInt(hostNport[1]), Protocol.DEFAULT_TIMEOUT, null,
				this.DataBase);
	}

	/**
	 * 从连接池中获取连接
	 * 
	 * @return
	 */
	private Jedis GetResource() {
		return this.RedisClientPool.getResource();
	}

	/**
	 * 字符串转换成字节数组
	 * 
	 * @param str
	 * @return
	 */
	private byte[] StringToBytes(String str) {
		return ConvertHelper.StringToBytes(str, this.Encoding);
	}

	/**
	 * 字节数组转换成字符串
	 * 
	 * @param bytes
	 * @return
	 */
	private String BytesToString(byte[] bytes) {
		return ConvertHelper.BytesToString(bytes, this.Encoding);
	}

	/**
	 * Object 转换成 BSON
	 * 
	 * @param obj
	 *            需要转换的对象
	 * @return
	 */
	private byte[] ObjectToBson(Object obj) {
		try {
			return this.StringToBytes(JsonHelper.toJson(obj));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * BSON 转换成 Object
	 * 
	 * @param bytes
	 *            JSON字符串的字节数组
	 * @param className
	 *            要转换的对象类型 MyClass.class
	 * @return
	 */
	private <T> T BsonToObject(byte[] bytes, Class<T> className) {
		try {
			return JsonHelper.toObject(this.BytesToString(bytes), className);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> List<T> GetList(String key, Class<T> className) {
		try {
			String jsonStr = this.GetString(key);
			if (!StringHelper.IsNullOrEmpty(jsonStr))
				return JsonHelper.toList(jsonStr, className);
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> T Get(String key, Class<T> className) {
		try {
			byte[] bytes = this.GetBytes(key);
			if (bytes != null)
				return this.BsonToObject(bytes, className);
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String GetString(String key) {
		byte[] bytes = this.GetBytes(key);
		if (bytes != null)
			return this.BytesToString(bytes);
		else
			return null;
	}

	@Override
	public byte[] GetBytes(String key) {
		return this._get(key);
	}

	private byte[] _get(String key) {
		Jedis redisClient = null;
		try {
			redisClient = this.GetResource();
			return redisClient.get(this.StringToBytes(key));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisClient.close();
		}
		return null;
	}

	@Override
	public <T> Boolean Set(String key, List<T> ListObject) {
		return this._set(key, this.ObjectToBson(ListObject), -1);
	}

	@Override
	public <T> Boolean Set(String key, List<T> ListObject, int cacheMinutes) {
		return this._set(key, this.ObjectToBson(ListObject), cacheMinutes);
	}

	@Override
	public <T> Boolean Set(String key, T targetObject) {
		return this._set(key, this.ObjectToBson(targetObject), -1);
	}

	@Override
	public <T> Boolean Set(String key, T targetObject, int cacheMinutes) {
		return this._set(key, this.ObjectToBson(targetObject), cacheMinutes);
	}

	@Override
	public Boolean Set(String key, String text) {
		return this._set(key, this.StringToBytes(text), -1);
	}

	@Override
	public Boolean Set(String key, String text, int cacheMinutes) {
		return this._set(key, this.StringToBytes(text), cacheMinutes);
	}

	@Override
	public Boolean Set(String key, byte[] bytes) {
		return this._set(key, bytes, -1);
	}

	@Override
	public Boolean Set(String key, byte[] bytes, int cacheMinutes) {
		return this._set(key, bytes, cacheMinutes);
	}

	private <T> Boolean _set(String key, byte[] bytes, int cacheMinutes) {
		Jedis redisClient = null;
		try {
			redisClient = this.GetResource();
			byte[] keyArray = this.StringToBytes(key);
			if (cacheMinutes <= 0) {
				return RedisResult.OK.equals(redisClient.set(keyArray, bytes));
			} else {
				return RedisResult.OK.equals(redisClient.setex(keyArray, cacheMinutes * 60, bytes));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisClient.close();
		}
		return false;
	}

	@Override
	public Boolean Expire(String key, int seconds) {
		Jedis redisClient = null;
		try {
			redisClient = this.GetResource();
			byte[] keyArray = this.StringToBytes(key);
			return RedisResult.SUCCESS.equals(redisClient.expire(keyArray, seconds));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisClient.close();
		}
		return false;
	}

	@Override
	public long TTL(String key) {
		Jedis redisClient = null;
		try {
			redisClient = this.GetResource();
			byte[] keyArray = this.StringToBytes(key);
			return redisClient.ttl(keyArray);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisClient.close();
		}
		return 0;
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
