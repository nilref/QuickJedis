package org.quickjedis.config;

import org.w3c.dom.Node;

import org.quickjedis.core.XmlHelper;

public class GlobalConfig {
	public Boolean ThrowException;

	public Boolean IsLog;

	public String InnerLogPath;

	public Boolean getThrowException() {
		return ThrowException;
	}

	public void setThrowException(Boolean throwException) {
		ThrowException = throwException;
	}

	public Boolean getIsLog() {
		return IsLog;
	}

	public void setIsLog(Boolean isLog) {
		IsLog = isLog;
	}

	public String getInnerLogPath() {
		return InnerLogPath;
	}

	public void setInnerLogPath(String innerLogPath) {
		InnerLogPath = innerLogPath;
	}

	public GlobalConfig(Node node) {
		if (node == null)
			return;
		this.ThrowException = XmlHelper.GetNodeAttr(node, "throw-exception").toLowerCase() == "true";
		this.IsLog = !(XmlHelper.GetNodeAttr(node, "is-log").toLowerCase() == "false");
		this.InnerLogPath = XmlHelper.GetNodeAttr(node, "inner-log-path").toLowerCase();
	}

	public GlobalConfig(Boolean ThrowException, Boolean IsLog, String InnerLogPath) {
		this.ThrowException = ThrowException;
		this.IsLog = IsLog;
		this.InnerLogPath = InnerLogPath;
	}

	public GlobalConfig() {
	}
}
