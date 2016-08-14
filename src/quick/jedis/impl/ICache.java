package quick.jedis.impl;

public interface ICache {

	Boolean Exists(String key);

	// T Get<T>(String key, Func<T> initItemFunc, int cacheMinutes );

	Boolean Remove(String key);

	//Boolean Set(String key, String targetObject, int cacheMinutes);

	<T> Boolean Set(String key, T targetObject, int cacheMinutes);

	//String Get(String key);

	<T> T Get(String key);
}