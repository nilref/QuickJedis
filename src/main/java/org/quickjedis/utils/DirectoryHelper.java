package org.quickjedis.utils;

import java.io.File;

public class DirectoryHelper {

	public static final String FilePathSplit = EnvironmentHelper.GetOSName().startsWith("Unix")
			|| EnvironmentHelper.GetOSName().startsWith("Linux") ? "/" : "\\";

	public static Boolean Exists(String path) {
		File file = new File(path);
		return file.exists() || file.isDirectory();
	}

	public static File CreateDirectory(String path) {
		File dir = new File(path);
		return dir.mkdir() ? dir : null;
	}
}
