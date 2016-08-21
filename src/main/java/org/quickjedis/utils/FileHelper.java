package org.quickjedis.utils;

import java.io.File;

public class FileHelper {

	public static Boolean Exists(String file) {
		return new File(file).exists();
	}

	public static String GetExtension(File file) {
		String fullname = GetFullName(file);
		String fileStr = fullname.substring(fullname.lastIndexOf("/"));
		return fileStr.substring(fileStr.indexOf("."));
	}

	public static String GetFullName(File file) {
		return file.getAbsoluteFile().getPath();
	}

	public static String GetClassesPath() {
		return FileHelper.class.getResource("/").toString().replace("file:/", "");
	}
}
