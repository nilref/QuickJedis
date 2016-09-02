package org.quickjedis.utils;

public class EnvironmentHelper {

	public static String GetNewLine() {
		return System.getProperty("line.separator");
	}

	public static String GetSafeEnvironmentVariable(String name) {
		try {
			return System.getProperty(name);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String GetOSName() {
		// 系统属性
		return System.getProperty("os.name");
	}

	public static String GetDefaultEncoding() {
		return System.getProperty("file.encoding");
	}
}