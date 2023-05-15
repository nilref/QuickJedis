package org.quickjedis.config;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;

import org.quickjedis.core.InnerLogger;
import org.quickjedis.core.Unity;
import org.quickjedis.core.XmlHelper;
import org.quickjedis.utils.DirectoryHelper;
import org.quickjedis.utils.FileHelper;
import org.quickjedis.utils.StringHelper;
import org.w3c.dom.Node;

public class ConfigManager {

    public static XmlCachingConfig CurrentConfig;

    public static void InitCacheConfig() {

        XmlCachingConfig xmlCachingConfig = new XmlCachingConfig();
        Node configNode = null;
        String filename = "quick-jedis-config.xml";
//        String classesPath = FileHelper.GetClassesPath();
//        final String str = (DirectoryHelper.FilePathSplit.startsWith("/") ? "/" : "")
//                + Paths.get(classesPath, filename).toString();
//        if (FileHelper.Exists(str)) {
//            configNode = XmlHelper.GetXmlNodeFromFile(str, "redis-root");
//            xmlCachingConfig = new XmlCachingConfig(configNode);
//        }
        InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(filename);
        if (is != null) {
            configNode = XmlHelper.GetXmlNodeFromFile(is, "redis-root");
            xmlCachingConfig = new XmlCachingConfig(configNode);
        }

        if (configNode == null)
            Unity.CreateException("configNode is null");
//        if (!StringHelper.IsNullOrEmpty(str)) {
//            ConfigManager.Observer = new FileAlterationObserver(classesPath, new FileFilter() {
//                @Override
//                public boolean accept(File f) {
//                    // 必须是指定的文件名才通过
//                    return f.getAbsoluteFile().getPath() == str;
//                }
//            });
//            ConfigManager.Observer.addListener(new ConfigFileListener());
//            long interval = 5000l; // 轮询间隔 5000 毫秒
//            FileAlterationMonitor monitor = new FileAlterationMonitor(interval, ConfigManager.Observer);
//            try {
//                monitor.start();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        ConfigManager.CurrentConfig = xmlCachingConfig;
    }

//    public static class ConfigFileListener extends FileAlterationListenerAdaptor {
//
//        @Override
//        public void onFileChange(File file) {
//            super.onFileChange(file);
//            try {
//                InnerLogger.Info(MessageFormat.format("ConfigFileChange FilePath:{0} FileName:{1}",
//                        file.getCanonicalPath(), file.getName()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                ConfigManager.InitCacheConfig();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


}