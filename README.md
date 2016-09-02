# QuickJedis
Quick start using jedis

快速上手Jedis，更多的关注业务开发

>在 src 下添加配置文件 quick-jedis-config.xml

``` xml
<?xml version="1.0" encoding="utf-8" ?>
<redis-root is-log="true" throw-exception="false" inner-log-path="/home/quick-jedis-log/">
    <redis-node name="HelloRedis" server="127.0.0.1:6379" db="2" encoding="UTF-8" />
    <redis-node name="TestRedis" server="127.0.0.1:6379" db="0" encoding="GBK" />
    ...
</redis-root>
```


><h4>配置说明：</h4>
>><b>name</b> : redis实例名称

>><b>server</b> : redis服务器IP:redis服务器端口

>><b>db</b> : redis数据库，从0到15

>><b>encoding</b> : 数据编码


><h4>示例代码</h4>
``` java
public class RedisDefined {
	public static Redis HelloRedis;
	public static Redis TestRedis;
	static {
		HelloRedis = CacheFactory.GetRedis("HelloRedis");
		TestRedis = CacheFactory.GetRedis("TestRedis");
	}
}
```

####操作字符串
``` java

// 写入
boolean res = RedisDefined.TestRedis.Set(key, jsonStr);
// 读取
String jsonStrTmp = RedisDefined.TestRedis.GetString(key);
```

####操作byte[]
``` java
// 写入
boolean bl = RedisDefined.TestRedis.Set(key, bytes);
// 读取
byte[] infoBytes = RedisDefined.TestRedis.GetBytes(key);
```

####操作Object
``` java
// 写入
Info info = new Info();
boolean bl = RedisDefined.TestRedis.Set(key, info);
// 读取
Info infoTmp = RedisDefined.TestRedis.Get(key, Info.class);
```


####操作List<T>
``` java
// 写入
List<Info> infoLst = new ArrayList<Info>();
boolean res = RedisDefined.TestRedis.Set(key, infoLst);
// 读取
List<Info> infoLstTmp = RedisDefined.TestRedis.GetList(key, Info.class);
```
