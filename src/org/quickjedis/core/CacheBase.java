package org.quickjedis.core;

public class CacheBase {
	private String CacheName;

	public String getCacheName() {
		return CacheName;
	}

	public void setCacheName(String cacheName) {
		CacheName = cacheName;
	}

	public CacheBase(String name) {
		this.CacheName = name;
	}

}