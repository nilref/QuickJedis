package quick.jedis.config;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;

import quick.jedis.core.InnerLogger;
import quick.jedis.core.RedisCache;
import quick.jedis.core.Unity;
import quick.jedis.core.XmlHelper;
import quick.jedis.impl.ICache;
import quick.jedis.utils.StringHelper;

public class XmlCachingConfig {
	public HashMap<String, ICache> CachePool = new HashMap<String, ICache>();
	private GlobalConfig _GlobalConfig;
	public List<Node> AllCacheXmlNodeList;

	public GlobalConfig GlobalConfig() {
		return this._GlobalConfig;
	}

	public XmlCachingConfig() {
	}

	public XmlCachingConfig(Node configNode) throws Exception {
		_xmlCachingConfig(configNode, "");
	}

	public XmlCachingConfig(Node configNode, String configFile) throws Exception {
		_xmlCachingConfig(configNode, configFile);
	}

	private void _xmlCachingConfig(Node configNode, String configFile) throws Exception {
		try {
			this.ConfigureGlobalConfigFromXml(XmlHelper.GetXmlNode(configNode, "global"));
			this.AllCacheXmlNodeList = XmlHelper.GetXmlNodes(configNode, "redis-node");
			this.ConfigureCachesFromXml(this.AllCacheXmlNodeList);
		} catch (Exception ex) {
			Unity.CreateException(ex.getMessage(), ex);
		}
	}

	private void ConfigureGlobalConfigFromXml(Node node) {
		if (node != null) {
			this._GlobalConfig = new GlobalConfig(node);
			if (this._GlobalConfig.IsLog)
				InnerLogger.InnerLogPath = this._GlobalConfig.InnerLogPath;
			InnerLogger.Info("*******************New Begin***********************");
			InnerLogger.Info("初始化配置开始");
			InnerLogger.Info("初始化全局配置对象成功");
		} else {
			this._GlobalConfig = new GlobalConfig(false, false, "");
			InnerLogger.Info("初始化全局配置对象失败，启用默认配置 [EX=未发现Global配置项!]");
		}
	}

	private void ConfigureCachesFromXml(List<Node> nodeList) {
		InnerLogger.Info("开始初始化缓存对象");
		if (nodeList != null) {
			for (Node xmlNode : nodeList) {
				String key = XmlHelper.GetNodeAttr(xmlNode, "name").toLowerCase();
				if (StringHelper.IsNullOrEmpty(key)) {
					InnerLogger.Info("初始化缓存对象[" + key + "] 失败，未配置Name节点");
				} else {
					InnerLogger.Debug("初始化缓存对象=>ConvertXmlToCache[" + key + "]");
					ICache cache = this.ConvertXmlToCache(xmlNode);
					this.CachePool.put(key, cache);
				}
			}
			InnerLogger.Info("初始化缓存对象成功 [CachesCount=" + this.CachePool.size() + "]");
		} else
			InnerLogger.Warn("初始化缓存对象失败 [EX=XmlNodeList is no childNode!]");
	}

	private ICache ConvertXmlToCache(Node xmlNode) {
		try {
			String nodeAttr = XmlHelper.GetNodeAttr(xmlNode, "name");
			String type = "redis";
			if ("redis" == type)
				return this.ConvertXmlToRedisCache(nodeAttr, type, xmlNode);
			InnerLogger.Error("初始化缓存对象警告：无效缓存类型 [Name=" + nodeAttr + "] [Type=" + type + "]");
			return (ICache) null;
		} catch (Exception ex) {
			InnerLogger.Error("初始化缓存目标发生异常 [Name=" + XmlHelper.GetNodeAttr(xmlNode, "name") + "] [Type="
					+ XmlHelper.GetNodeAttr(xmlNode, "type") + "] 异常信息：[" + ex.toString() + "]");
			return (ICache) null;
		}
	}

	private ICache ConvertXmlToRedisCache(String name, String type, Node xmlNode) {
		try {
			RedisCache redisCache = new RedisCache(xmlNode);
			InnerLogger.Info("初始化缓存对象[" + type + "][" + name + "] 成功");
			return (ICache) redisCache;
		} catch (Exception ex) {
			InnerLogger.Error("初始化缓存目标发生异常 ConvertXmlToRedisCache [Name=" + name + "][Type=" + type + "] 异常信息：["
					+ ex.toString() + "]");
			return (ICache) null;
		}
	}

}