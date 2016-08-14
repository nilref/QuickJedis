package quick.jedis.utils;

import java.io.File;

public class DirectoryHelper {

	public static Boolean Exists(String path) {
		File file = new File(path);
		return file.exists() || file.isDirectory();
	}

	public static File CreateDirectory(String path) {
		File dir = new File(path);
		return dir.mkdir() ? dir : null;
	}
}
