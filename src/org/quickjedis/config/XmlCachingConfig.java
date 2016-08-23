package org.quickjedis.config;

import java.util.HashMap;
import java.util.List;

import org.quickjedis.core.InnerLogger;
import org.quickjedis.core.Redis;
import org.quickjedis.core.RedisCache;
import org.quickjedis.core.Unity;
import org.quickjedis.core.XmlHelper;
import org.quickjedis.utils.StringHelper;
import org.w3c.dom.Node;

public class XmlCachingConfig {
	public HashMap<String, Redis> RedisPool = new HashMap<String, Redis>();
	private GlobalConfig _GlobalConfig;
	public List<Node> AllCacheXmlNodeList;

	public GlobalConfig GlobalConfig() {
		return this._GlobalConfig;
	}

	public XmlCachingConfig() {
	}

	public XmlCachingConfig(Node configNode) {
		try {
			this.ConfigureGlobalConfigFromXml(configNode);
			this.AllCacheXmlNodeList = XmlHelper.GetXmlNodes(configNode, "redis-node");
			this.ConfigureCachesFromXml(this.AllCacheXmlNodeList);
		} catch (Exception ex) {
			try {
				Unity.CreateException(ex.getMessage(), ex);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
					Redis cache = this.ConvertXmlToCache(xmlNode);
					this.RedisPool.put(key, cache);
				}
			}
			InnerLogger.Info("初始化缓存对象成功 [CachesCount=" + this.RedisPool.size() + "]");
		} else
			InnerLogger.Warn("初始化缓存对象失败 [EX=XmlNodeList is no childNode!]");
	}

	private Redis ConvertXmlToCache(Node xmlNode) {
		try {
			String nodeAttr = XmlHelper.GetNodeAttr(xmlNode, "name");
			return this.ConvertXmlToRedisCache(nodeAttr, "redis", xmlNode);
		} catch (Exception ex) {
			InnerLogger.Error("初始化缓存目标发生异常 [Name=" + XmlHelper.GetNodeAttr(xmlNode, "name") + "] [Type="
					+ XmlHelper.GetNodeAttr(xmlNode, "type") + "] 异常信息：[" + ex.toString() + "]");
			return (Redis) null;
		}
	}

	private Redis ConvertXmlToRedisCache(String name, String type, Node xmlNode) {
		try {
			RedisCache redisCache = new RedisCache(xmlNode);
			InnerLogger.Info("初始化缓存对象[" + type + "][" + name + "] 成功");
			return (Redis) redisCache;
		} catch (Exception ex) {
			InnerLogger.Error("初始化缓存目标发生异常 ConvertXmlToRedisCache [Name=" + name + "][Type=" + type + "] 异常信息：["
					+ ex.toString() + "]");
			return (Redis) null;
		}
	}

}