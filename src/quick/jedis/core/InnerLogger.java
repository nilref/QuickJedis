package quick.jedis.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import quick.jedis.utils.DateHelper;
import quick.jedis.utils.DirectoryHelper;
import quick.jedis.utils.EnvironmentHelper;
import quick.jedis.utils.FileHelper;
import quick.jedis.utils.StringHelper;

public class InnerLogger {
	private static Lock lock = new ReentrantLock();
	static String FilePathSplit = EnvironmentHelper.GetOSVersion().startsWith("Unix") ? "/" : "\\";
	public static String InnerLogPath;
	static String FileName;

	public static void Trace(String CustomerInfo) {
		InnerLogger.WriteLog("Trace", CustomerInfo);
	}

	public static void Debug(String CustomerInfo) {
		InnerLogger.WriteLog("Debug", CustomerInfo);
	}

	public static void Info(String CustomerInfo) {
		InnerLogger.WriteLog("Info", CustomerInfo);
	}

	public static void Error(String CustomerInfo) {
		InnerLogger.WriteLog("Error", CustomerInfo);
	}

	public static void Warn(String CustomerInfo) {
		InnerLogger.WriteLog("Warn", CustomerInfo);
	}

	public static void Fatal(String CustomerInfo) {
		InnerLogger.WriteLog("Fatal", CustomerInfo);
	}

	private static void WriteLog(String level, String customerInfo) {
		if (!StringHelper.IsNullOrEmpty(InnerLogger.InnerLogPath))
			InnerLogger.FileName = !InnerLogger.InnerLogPath.endsWith(InnerLogger.FilePathSplit)
					? InnerLogger.InnerLogPath + InnerLogger.FilePathSplit + "Qjedis.log"
					: InnerLogger.InnerLogPath + "Qjedis.log";
		try {
			File file = new File(InnerLogger.FileName);

			if (StringHelper.IsNullOrEmpty(file.getCanonicalPath()))
				InnerLogger.FileName = Paths
						.get(EnvironmentHelper.GetSafeEnvironmentVariable("usr.dir"), InnerLogger.FileName).toString();
			else if (!DirectoryHelper.Exists(file.getCanonicalPath()))
				DirectoryHelper.CreateDirectory(file.getCanonicalPath());
			Lock lock = InnerLogger.lock;
			Boolean lockTaken = false;
			try {
				lockTaken = lock.tryLock();
				try {

					if (file.length() >= 524288000L) {
						String destFileName = FileHelper.GetFullName(file).substring(0,
								FileHelper.GetFullName(file).length() - FileHelper.GetExtension(file).length()) + "-"
								+ DateHelper.GetDateNow("yyyy-MM-dd-hh-mm-ss-ffff") + FileHelper.GetExtension(file);
						Files.move(file.toPath(), new File(destFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
						file.createNewFile();
					}
				} catch (Exception ex) {
				}
			} finally {
				if (lockTaken)
					lock.unlock();
			}
			String str = DateHelper.GetDateNow("yyyy-MM-dd HH:mm:ss.ffff") + " " + "[" + level + "]" + " "
					+ customerInfo;
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(InnerLogger.FileName, true), EnvironmentHelper.GetDefaultEncoding()));
			out.write(str);
			out.newLine();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}