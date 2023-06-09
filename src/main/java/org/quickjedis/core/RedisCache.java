package org.quickjedis.core;

import java.util.*;

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
    private String password;

    public RedisCache(Node xmlNode) {
        this(XmlHelper.GetNodeAttr(xmlNode, "name"), XmlHelper.GetNodeAttr(xmlNode, "server"),
                XmlHelper.GetNodeAttr(xmlNode, "db"), XmlHelper.GetNodeAttr(xmlNode, "password"),
                XmlHelper.GetNodeAttr(xmlNode, "encoding"));
    }

    public RedisCache(final String name, final String server, final String db) {
        this(name, server, db, "", "UTF-8");
    }

    public RedisCache(final String name, final String server, final String db, final String pwd) {
        this(name, server, db, pwd, "UTF-8");
    }

    public RedisCache(final String name, final String server, final String db, final String pwd, final String encoding) {
        super(name);
        // 设置DB
        TryParseResult<Integer> tryRes = ConvertHelper.TryParseInt(db);
        if (!tryRes.getSuccess())
            this.DataBase = 0;
        else
            this.DataBase = tryRes.getObject();
        if (this.DataBase > 15 || this.DataBase < 0)
            this.DataBase = 0;

        // 设置密码
        if (StringHelper.IsNullOrEmpty(pwd)) {
            this.password = null;
        } else {
            this.password = pwd;
        }

        // 设置编码
        if (StringHelper.IsNullOrEmpty(encoding))
            this.Encoding = "UTF-8";
        else
            this.Encoding = encoding;
        this.RedisClientPool = this.CreateManager(server);
    }

    private JedisPool CreateManager(final String hosts) {

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
        return new JedisPool(config, hostNport[0], Integer.parseInt(hostNport[1]), Protocol.DEFAULT_TIMEOUT, this.password,
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
    private byte[] StringToBytes(final String str) {
        return ConvertHelper.StringToBytes(str, this.Encoding);
    }

    /**
     * 字节数组转换成字符串
     *
     * @param bytes
     * @return
     */
    private String BytesToString(final byte[] bytes) {
        return ConvertHelper.BytesToString(bytes, this.Encoding);
    }

    /**
     * Object 转换成 BSON
     *
     * @param obj 需要转换的对象
     * @return
     */
    private byte[] ObjectToBson(final Object obj) {
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
     * @param bytes     JSON字符串的字节数组
     * @param className 要转换的对象类型 MyClass.class
     * @return
     */
    private <T> T BsonToObject(final byte[] bytes, final Class<T> className) {
        try {
            return JsonHelper.toObject(this.BytesToString(bytes), className);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> List<T> GetList(final String key, final Class<T> className) {
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
    public <T> T Get(final String key, final Class<T> className) {
        try {
            byte[] bytes = this.GetBytes(key);
            if (bytes != null)
                return this.BsonToObject(bytes, className);
            else
                return ConvertHelper.GetDefaultVal(className);
        } catch (Exception e) {
            e.printStackTrace();
            return ConvertHelper.GetDefaultVal(className);
        }
    }

    @Override
    public String GetString(final String key) {
        byte[] bytes = this.GetBytes(key);
        if (bytes != null)
            return this.BytesToString(bytes);
        else
            return "";
    }

    @Override
    public byte[] GetBytes(final String key) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.get(this.StringToBytes(key));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public <T> boolean Set(final String key, final List<T> ListObject) {
        return this.Set(key, this.ObjectToBson(ListObject), -1);
    }

    @Override
    public <T> boolean Set(final String key, final List<T> ListObject, final int cacheMinutes) {
        return this.Set(key, this.ObjectToBson(ListObject), cacheMinutes);
    }

    @Override
    public <T> boolean Set(final String key, final T targetObject) {
        return this.Set(key, this.ObjectToBson(targetObject), -1);
    }

    @Override
    public <T> boolean Set(final String key, final T targetObject, final int cacheMinutes) {
        return this.Set(key, this.ObjectToBson(targetObject), cacheMinutes);
    }

    @Override
    public boolean Set(final String key, final String text) {
        return this.Set(key, this.StringToBytes(text), -1);
    }

    @Override
    public boolean Set(final String key, final String text, final int cacheMinutes) {
        return this.Set(key, this.StringToBytes(text), cacheMinutes);
    }

    @Override
    public boolean Set(final String key, byte[] bytes) {
        return this.Set(key, bytes, -1);
    }

    @Override
    public boolean Set(final String key, byte[] bytes, final int cacheMinutes) {
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
            if (redisClient != null)
                redisClient.close();
        }
        return false;
    }

    @Override
    public boolean Expire(final String key, final int seconds) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return RedisResult.SUCCESS.equals(redisClient.expire(keyArray, seconds));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return false;
    }

    @Override
    public long TTL(final String key) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return redisClient.ttl(keyArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long Del(final String key) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return redisClient.del(keyArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> T Hget(final String key, final String field, final Class<T> className) {
        try {
            byte[] bytes = this.HgetBytes(key, field);
            if (bytes != null)
                return this.BsonToObject(bytes, className);
            else
                return ConvertHelper.GetDefaultVal(className);
        } catch (Exception e) {
            e.printStackTrace();
            return ConvertHelper.GetDefaultVal(className);
        }
    }

    @Override
    public String HgetString(final String key, final String field) {
        try {
            return this.BytesToString(this.HgetBytes(key, field));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public byte[] HgetBytes(final String key, final String field) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            byte[] fieldArray = this.StringToBytes(field);
            return redisClient.hget(keyArray, fieldArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public boolean Hset(final String key, final String field, final String value) {
        return this.Hset(key, field, this.StringToBytes(value));
    }

    @Override
    public boolean Hset(final String key, final String field, byte[] value) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            byte[] fieldArray = this.StringToBytes(field);
            return RedisResult.SUCCESS.equals(redisClient.hset(keyArray, fieldArray, value));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return false;
    }

    @Override
    public long HincrBy(final String key, final String field, final long increment) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            byte[] fieldArray = this.StringToBytes(field);
            return redisClient.hincrBy(keyArray, fieldArray, increment);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> HashMap<Double, T> ZrangeWithScores(final String key, final long start, final long stop,
                                                   final Class<T> className) {
        try {
            Set<Tuple> sets = this.ZrangeWithScores(key, start, stop);
            HashMap<Double, T> hashMap = new HashMap<Double, T>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(tuple.getScore(), this.BsonToObject(tuple.getBinaryElement(), className));
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public Set<Tuple> ZrangeWithScores(final String key, final long start, final long stop) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return redisClient.zrangeWithScores(keyArray, start, stop);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public <T> Set<T> Zrange(final String key, final long start, final long stop, final Class<T> className) {
        try {
            Set<byte[]> setBytes = this.Zrange(key, start, stop);
            Set<T> sets = new HashSet<T>();
            for (final byte[] bytes : setBytes) {
                sets.add(this.BsonToObject(bytes, className));
            }
            return sets;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<byte[]> Zrange(final String key, final long start, final long stop) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return redisClient.zrange(keyArray, start, stop);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public long LLen(final String queueId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 将一个值插入到列表头部
     *
     * @param queueId
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public <T> long LPush(final String queueId, final T value) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            return redisClient.lpush(keyArray, this.ObjectToBson(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> long LPush(final String queueId, final T... valueArray) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            List<byte[]> valueList = new ArrayList<byte[]>();
            for (T value : valueArray) {
                valueList.add(this.ObjectToBson(value));
            }
            return redisClient.lpush(keyArray, valueList.toArray(new byte[0][]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> T RPop(final String queueId, final Class<T> className) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            byte[] bytes =  redisClient.rpop(keyArray);
            if (bytes != null) {
                return this.BsonToObject(bytes, className);
            }
            else {
                return ConvertHelper.GetDefaultVal(className);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return ConvertHelper.GetDefaultVal(className);
    }

    @Override
    public String LIndex(final String listId, final int listIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long SCARD(final String setid) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long SADD(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long SREM(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String SPOP(final String setid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> SMEMBERS(final String setid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long SISMEMBER(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void SMOVE(final String setid, final String toSetid, final String member) {
        // TODO Auto-generated method stub

    }

    @Override
    public long ZCARD(final String setid) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long ZADD(final String setid, final String member, final int score) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long ZREM(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double ZSCORE(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double ZINCRBY(final String setid, final String member, final int increment) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Tuple> ZRANGE(final String setid, final int start, final int stop, boolean withScore) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Tuple> ZREVRANGE(final String setid, final int start, final int stop, boolean withScore) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Tuple> ZRANGEBYSCORE(final String setid, final double min, final double max, final int skip,
                                     final int take, boolean withScore) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long ZREMRANGEBYRANK(final String setid, final int min, final int max) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long HIncrby(final String hashId, final String field, final int incrementBy) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long HSet(final String hashId, final String field, final String value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void HMSet(final String hashId, final List<String> keyList, final List<String> valueList) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> HMGet(final String hashId, final List<String> keyList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String HGet(final String hashId, final String field) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> HGetValues(final String hashId, final List<String> keys) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dictionary<String, String> HGetAll(final String hashId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long HDel(final String hashId, final String field) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long HLen(final String hashId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Incr 命令将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @return
     */
    @Override
    public long Incr(final String key) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.incr(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long Decr(final String key) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long Increment(final String key, final int amount) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long Decrement(final String key, final int amount) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long IncrementValueInHash(final String hashId, final String key, final int incrementBy) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Dictionary<String, String> Info() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean Ping() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<String> SearchKeys(final String pattern) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean Exists(final String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean Remove(final String key) {
        // TODO Auto-generated method stub
        return false;
    }

}
