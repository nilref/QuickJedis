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
			InnerLogger.Info("===========Begin===========");
			InnerLogger.Info("Init config start");
			InnerLogger.Info("Init global config success");
		} else {
			this._GlobalConfig = new GlobalConfig(false, false, "");
			InnerLogger.Info("Init global config fail, enabled default config [EX=Not found global config!]");
		}
	}

	private void ConfigureCachesFromXml(List<Node> nodeList) {
		InnerLogger.Info("Init cached object start");
		if (nodeList != null) {
			for (Node xmlNode : nodeList) {
				String key = XmlHelper.GetNodeAttr(xmlNode, "name").toLowerCase();
				if (StringHelper.IsNullOrEmpty(key)) {
					InnerLogger.Info("Init cached object[" + key + "] fail, not found \"Name\" node");
				} else {
					InnerLogger.Debug("Init cached object=>ConvertXmlToCache[" + key + "]");
					Redis cache = this.ConvertXmlToCache(xmlNode);
					this.RedisPool.put(key, cache);
				}
			}
			InnerLogger.Info("Init cached object success [CachesCount=" + this.RedisPool.size() + "]");
		} else
			InnerLogger.Warn("Init cached object fail [EX=XmlNodeList is no childNode!]");
	}

	private Redis ConvertXmlToCache(Node xmlNode) {
		try {
			String nodeAttr = XmlHelper.GetNodeAttr(xmlNode, "name");
			return this.ConvertXmlToRedisCache(nodeAttr, "redis", xmlNode);
		} catch (Exception ex) {
			InnerLogger.Error("Init cached object error [Name=" + XmlHelper.GetNodeAttr(xmlNode, "name") + "] [Type="
					+ XmlHelper.GetNodeAttr(xmlNode, "type") + "] error: [" + ex.toString() + "]");
			return (Redis) null;
		}
	}

	private Redis ConvertXmlToRedisCache(String name, String type, Node xmlNode) {
		try {
			RedisCache redisCache = new RedisCache(xmlNode);
			InnerLogger.Info("Init cached object[" + type + "][" + name + "] success");
			return (Redis) redisCache;
		} catch (Exception ex) {
			InnerLogger.Error("Init cached object error ConvertXmlToRedisCache [Name=" + name + "][Type=" + type
					+ "] error: [" + ex.toString() + "]");
			return (Redis) null;
		}
	}

}