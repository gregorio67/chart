package dymn.utils;

import java.net.URL;
import java.util.Iterator;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReloadPropertyUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadPropertyUtil.class);
	private static PropertiesConfiguration configuration;
	private static final int refreshDelay = 1000;
	
	static {

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("properties/system.properties");
			synchronized(ReloadPropertyUtil.class) {
				configuration = new PropertiesConfiguration(url);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		FileChangedReloadingStrategy reloadStrategy = new FileChangedReloadingStrategy();
		reloadStrategy.setRefreshDelay(refreshDelay);
		configuration.setReloadingStrategy(reloadStrategy);
		
	}
	
    public static String getString(String key) {
        return configuration.getString(key);
    }
    
    public static int getInt(String key) {
        return configuration.getInt(key);
    }
    
    public static long getLong(String key) {
        return configuration.getLong(key);
    }

}
