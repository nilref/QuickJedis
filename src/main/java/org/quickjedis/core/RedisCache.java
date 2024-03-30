package org.quickjedis.core;

import java.util.*;

import org.quickjedis.model.RedisResult;
import org.quickjedis.utils.ConvertHelper;
import org.quickjedis.utils.ConvertHelper.TryParseResult;
import org.quickjedis.utils.JsonUtil;
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
            return this.StringToBytes(JsonUtil.toJson(obj));
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
            return JsonUtil.toObject(this.BytesToString(bytes), className);
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
                return JsonUtil.toList(jsonStr, className);
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
            return ConvertHelper.GetDefaultVal(String.class);
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
    public Boolean HExists(final String hashId, final String field) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.hexists(hashId, field);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public <T> T HGet(final String key, final String field, final Class<T> className) {
        try {
            byte[] bytes = this.hGetBytes(key, field);
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
    public String HGet(final String key, final String field) {
        try {
            byte[] bytes = this.hGetBytes(key, field);
            if (bytes != null)
                return this.BytesToString(bytes);
            else
                return ConvertHelper.GetDefaultVal(String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ConvertHelper.GetDefaultVal(String.class);
        }
    }

    private byte[] hGetBytes(final String key, final String field) {
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
    public long HSet(final String key, final String field, final String value) {
        return this.HSet(key, field, this.StringToBytes(value));
    }

    @Override
    public <T> long HSet(final String key, final String field, final T value) {
        return this.HSet(key, field, this.ObjectToBson(value));
    }

    private long HSet(final String key, final String field, byte[] value) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            byte[] fieldArray = this.StringToBytes(field);
            return redisClient.hset(keyArray, fieldArray, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long HIncrBy(final String key, final String field, final long increment) {
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
    public void HMSet(final String hashId, final List<String> keyList, final List<String> valueList) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> HMGet(final String hashId, final List<String> keyList) {
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
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.hdel(hashId, field);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long HLen(final String hashId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long LLen(final String queueId) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.llen(queueId);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long LPush(final String queueId, final String value) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            return redisClient.lpush(keyArray, this.StringToBytes(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

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
    public long LPush(final String queueId, final String... valueArray) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            List<byte[]> valueList = new ArrayList<byte[]>();
            for (String value : valueArray) {
                valueList.add(this.StringToBytes(value));
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
    public long RPush(final String queueId, final String value) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            return redisClient.rpush(keyArray, this.StringToBytes(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> long RPush(final String queueId, final T value) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            return redisClient.rpush(keyArray, this.ObjectToBson(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long RPush(final String queueId, final String... valueArray) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            List<byte[]> valueList = new ArrayList<byte[]>();
            for (String value : valueArray) {
                valueList.add(this.StringToBytes(value));
            }
            return redisClient.rpush(keyArray, valueList.toArray(new byte[0][]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> long RPush(final String queueId, final T... valueArray) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            List<byte[]> valueList = new ArrayList<byte[]>();
            for (T value : valueArray) {
                valueList.add(this.ObjectToBson(value));
            }
            return redisClient.rpush(keyArray, valueList.toArray(new byte[0][]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public String RPop(final String queueId) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            byte[] bytes = redisClient.rpop(keyArray);
            if (bytes != null) {
                return this.BytesToString(bytes);
            } else {
                return ConvertHelper.GetDefaultVal(String.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return ConvertHelper.GetDefaultVal(String.class);
    }

    @Override
    public <T> T RPop(final String queueId, final Class<T> className) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(queueId);
            byte[] bytes = redisClient.rpop(keyArray);
            if (bytes != null) {
                return this.BsonToObject(bytes, className);
            } else {
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
    public long SCard(final String setid) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            Long len = redisClient.scard(setid);
            if (len != null)
                return len;
            else
                return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long SAdd(final String setid, final String... member) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            List<byte[]> valueList = new ArrayList<byte[]>();
            for (String value : member) {
                valueList.add(this.StringToBytes(value));
            }
            return redisClient.sadd(keyArray, valueList.toArray(new byte[0][]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> long SAdd(final String setid, final T... member) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            List<byte[]> valueList = new ArrayList<byte[]>();
            for (T value : member) {
                valueList.add(this.ObjectToBson(value));
            }
            return redisClient.sadd(keyArray, valueList.toArray(new byte[0][]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long SREM(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String SPop(final String setid) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            byte[] bytes = redisClient.spop(keyArray);
            if (bytes != null) {
                return this.BytesToString(bytes);
            } else {
                return ConvertHelper.GetDefaultVal(String.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return ConvertHelper.GetDefaultVal(String.class);
    }

    @Override
    public <T> T SPop(final String setid, final Class<T> className) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            byte[] bytes = redisClient.spop(keyArray);
            if (bytes != null) {
                return this.BsonToObject(bytes, className);
            } else {
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
    public List<String> SMembers(final String setid) {
        List<String> list = new ArrayList<String>();
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            Set<byte[]> set = redisClient.smembers(keyArray);
            if (set != null) {
                for (byte[] bytes : set) {
                    list.add(this.BytesToString(bytes));
                }
                return list;
            } else {
                return list;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public <T> List<T> SMembers(final String setid, final Class<T> className) {
        List<T> list = new ArrayList<T>();
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            Set<byte[]> set = redisClient.smembers(keyArray);
            if (set != null) {
                for (byte[] bytes : set) {
                    list.add(this.BsonToObject(bytes, className));
                }
                return list;
            } else {
                return list;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public List<String> SRandMember(final String setid, int count) {
        List<String> list = new ArrayList<String>();
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            List<byte[]> set = redisClient.srandmember(keyArray, count);
            if (set != null) {
                for (byte[] bytes : set) {
                    list.add(this.BytesToString(bytes));
                }
                return list;
            } else {
                return list;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public <T> List<T> SRandMember(final String setid, int count, final Class<T> className) {
        List<T> list = new ArrayList<T>();
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(setid);
            List<byte[]> set = redisClient.srandmember(keyArray, count);
            if (set != null) {
                for (byte[] bytes : set) {
                    list.add(this.BsonToObject(bytes, className));
                }
                return list;
            } else {
                return list;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
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
    public long ZCard(final String setid) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            Long count = redisClient.zcard(setid);
            if (count != null)
                return count;
            else
                return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long ZCount(final String setid, final double min, final double max) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            Long count = redisClient.zcount(setid, min, max);
            if (count != null)
                return count;
            else
                return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long ZAdd(final String setid, final String member, final double score) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            byte[] memberBytes = this.StringToBytes(member);
            return redisClient.zadd(keyBytes, score, memberBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> long ZAdd(final String setid, final T member, final double score) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            byte[] memberBytes = this.ObjectToBson(member);
            return redisClient.zadd(keyBytes, score, memberBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public double ZScore(final String setid, final String member) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            byte[] memberBytes = this.StringToBytes(member);
            Double score = redisClient.zscore(keyBytes, memberBytes);
            if (score != null)
                return score;
            else
                return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public <T> double ZScore(final String setid, final T member) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            byte[] memberBytes = this.ObjectToBson(member);
            Double score = redisClient.zscore(keyBytes, memberBytes);
            if (score != null)
                return score;
            else
                return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return 0;
    }

    @Override
    public long ZRank(final String setid, final String member) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            byte[] memberBytes = this.StringToBytes(member);
            Long rank = redisClient.zrank(keyBytes, memberBytes);
            if (rank != null)
                return rank;
            else
                return -1;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return -1;
    }

    @Override
    public <T> long ZRank(final String setid, final T member) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            byte[] memberBytes = this.ObjectToBson(member);
            Long rank = redisClient.zrank(keyBytes, memberBytes);
            if (rank != null)
                return rank;
            else
                return -1;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return -1;
    }

    @Override
    public long ZREM(final String setid, final String member) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double ZINCRBY(final String setid, final String member, final int increment) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public HashMap<String, Double> ZRangeWithScores(final String key, final long start, final long stop) {
        try {
            Set<Tuple> sets = this.zRangeWithScores(key, start, stop);
            HashMap<String, Double> hashMap = new HashMap<String, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BytesToString(tuple.getBinaryElement()), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public <T> HashMap<T, Double> ZRangeWithScores(final String key, final long start, final long stop,
                                                   final Class<T> className) {
        try {
            Set<Tuple> sets = this.zRangeWithScores(key, start, stop);
            HashMap<T, Double> hashMap = new HashMap<T, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BsonToObject(tuple.getBinaryElement(), className), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private Set<Tuple> zRangeWithScores(final String key, final long start, final long stop) {
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
    public Set<String> ZRange(final String key, final long start, final long stop) {
        try {
            Set<byte[]> setBytes = this.zRange(key, start, stop);
            Set<String> sets = new HashSet<String>();
            for (final byte[] bytes : setBytes) {
                sets.add(this.BytesToString(bytes));
            }
            return sets;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> Set<T> ZRange(final String key, final long start, final long stop, final Class<T> className) {
        try {
            Set<byte[]> setBytes = this.zRange(key, start, stop);
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

    private Set<byte[]> zRange(final String key, final long start, final long stop) {
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
    public HashMap<String, Double> ZRevRangeWithScores(final String setid, final long start, final long stop) {
        try {
            Set<Tuple> sets = this.zRevRangeWithScores(setid, start, stop);
            HashMap<String, Double> hashMap = new HashMap<String, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BytesToString(tuple.getBinaryElement()), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public <T> HashMap<T, Double> ZRevRangeWithScores(final String setid, final long start, final long stop,
                                                      final Class<T> className) {
        try {
            Set<Tuple> sets = this.zRevRangeWithScores(setid, start, stop);
            HashMap<T, Double> hashMap = new HashMap<T, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BsonToObject(tuple.getBinaryElement(), className), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private Set<Tuple> zRevRangeWithScores(final String setid, final long start, final long stop) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.zrevrangeWithScores(setid, start, stop);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public Set<String> ZRevRange(final String setid, final long start, final long stop) {
        try {
            Set<byte[]> setBytes = this.zRevRange(setid, start, stop);
            Set<String> sets = new HashSet<String>();
            for (final byte[] bytes : setBytes) {
                sets.add(this.BytesToString(bytes));
            }
            return sets;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> Set<T> ZRevRange(final String setid, final long start, final long stop, final Class<T> className) {
        try {
            Set<byte[]> setBytes = this.zRevRange(setid, start, stop);
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

    private Set<byte[]> zRevRange(final String setid, final long start, final long stop) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            return redisClient.zrevrange(keyBytes, start, stop);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public HashMap<String, Double> ZRangeByScoreWithScores(final String key, final double min, final double max) {
        try {
            Set<Tuple> sets = this.zRangeByScoreWithScores(key, min, max);
            HashMap<String, Double> hashMap = new HashMap<String, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BytesToString(tuple.getBinaryElement()), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> HashMap<T, Double> ZRangeByScoreWithScores(final String key, final double min, final double max,
                                                          final Class<T> className) {
        try {
            Set<Tuple> sets = this.zRangeByScoreWithScores(key, min, max);
            HashMap<T, Double> hashMap = new HashMap<T, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BsonToObject(tuple.getBinaryElement(), className), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Set<Tuple> zRangeByScoreWithScores(final String key, final double min, final double max) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return redisClient.zrangeByScoreWithScores(keyArray, min, max);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public Set<String> ZRangeByScore(final String key, final double min, final double max) {
        try {
            Set<byte[]> setBytes = this.zRangeByScore(key, min, max);
            Set<String> sets = new HashSet<String>();
            for (final byte[] bytes : setBytes) {
                sets.add(this.BytesToString(bytes));
            }
            return sets;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> Set<T> ZRangeByScore(final String key, final double min, final double max, final Class<T> className) {
        try {
            Set<byte[]> setBytes = this.zRangeByScore(key, min, max);
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

    private Set<byte[]> zRangeByScore(final String key, final double min, final double max) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyArray = this.StringToBytes(key);
            return redisClient.zrangeByScore(keyArray, min, max);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public HashMap<String, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min) {
        try {
            Set<Tuple> sets = this.zRevRangeByScoreWithScores(setid, max, min);
            HashMap<String, Double> hashMap = new HashMap<String, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BytesToString(tuple.getBinaryElement()), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> HashMap<T, Double> ZRevRangeByScoreWithScores(final String setid, final double max, final double min,
                                                             final Class<T> className) {
        try {
            Set<Tuple> sets = this.zRevRangeByScoreWithScores(setid, max, min);
            HashMap<T, Double> hashMap = new HashMap<T, Double>();
            Object[] objs = sets.toArray();
            for (Object obj : objs) {
                Tuple tuple = (Tuple) obj;
                hashMap.put(this.BsonToObject(tuple.getBinaryElement(), className), tuple.getScore());
            }
            return hashMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Set<Tuple> zRevRangeByScoreWithScores(final String setid, final double max, final double min) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            return redisClient.zrevrangeByScoreWithScores(setid, max, min);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public Set<String> ZRevRangeByScore(final String setid, final double max, final double min) {
        try {
            Set<byte[]> setBytes = this.zRevRangeByScore(setid, max, min);
            Set<String> sets = new HashSet<String>();
            for (final byte[] bytes : setBytes) {
                sets.add(this.BytesToString(bytes));
            }
            return sets;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> Set<T> ZRevRangeByScore(final String setid, final double max, final double min, final Class<T> className) {
        try {
            Set<byte[]> setBytes = this.zRevRangeByScore(setid, max, min);
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

    private Set<byte[]> zRevRangeByScore(final String setid, final double max, final double min) {
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            byte[] keyBytes = this.StringToBytes(setid);
            return redisClient.zrevrangeByScore(keyBytes, max, min);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
        return null;
    }

    @Override
    public long ZREMRANGEBYRANK(final String setid, final int min, final int max) {
        // TODO Auto-generated method stub
        return 0;
    }

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
    public List<String> Keys(final String pattern) {
        List<String> list = new ArrayList<String>();
        Jedis redisClient = null;
        try {
            redisClient = this.GetResource();
            Set<String> set = redisClient.keys(pattern);
            if (set != null) {
                for (String key : set) {
                    list.add(key);
                }
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (redisClient != null)
                redisClient.close();
        }
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
