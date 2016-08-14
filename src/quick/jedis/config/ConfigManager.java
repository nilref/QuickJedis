package quick.jedis.config;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.w3c.dom.Node;

import quick.jedis.core.InnerLogger;
import quick.jedis.core.Unity;
import quick.jedis.core.XmlHelper;
import quick.jedis.utils.FileHelper;
import quick.jedis.utils.StringHelper;

public class ConfigManager {	

	private static FileAlterationObserver Observer;
	
	public static XmlCachingConfig CurrentConfig;

	public static void InitCacheConfig() throws Exception  {
		
		XmlCachingConfig xmlCachingConfig = new XmlCachingConfig();
		File file = new File("");
		String filename ="QjedisConfig.xml";
		final String str = Paths.get(file.getCanonicalPath(), filename).toString();
		Node configNode = null;
		if (FileHelper.Exists(str)) {
			configNode = XmlHelper.GetXmlNodeFromFile(str, "redis-root");
			xmlCachingConfig = new XmlCachingConfig(configNode, str);
		}

		if (configNode == null)
			Unity.CreateException("", new Exception(""));
		if (!StringHelper.IsNullOrEmpty(str)) {
			ConfigManager.Observer=new FileAlterationObserver(file.getCanonicalPath(),new FileFilter() {  
	            @Override  
	            public boolean accept(File f) {
	            	//必须是指定的文件名才通过
	                return f.getAbsoluteFile().getPath()==str;  
	            }  
	        });
			ConfigManager.Observer.addListener(new ConfigFileListener());  
			long interval = 5000l; //轮询间隔  5000 毫秒 
			FileAlterationMonitor monitor = new FileAlterationMonitor(interval,ConfigManager.Observer);   
			monitor.start();
		}

		ConfigManager.CurrentConfig = xmlCachingConfig;
	}

	public static class ConfigFileListener extends FileAlterationListenerAdaptor{

		@Override
		public void onFileChange(File file) {
			super.onFileChange(file);
			try {
				InnerLogger.Info(MessageFormat.format("ConfigFileChange FilePath:{0} FileName:{1}",
				 file.getCanonicalPath(), file.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				ConfigManager.InitCacheConfig();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}   
		
	}
}