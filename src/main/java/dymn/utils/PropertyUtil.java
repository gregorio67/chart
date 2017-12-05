package dymn.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyUtil extends PropertyPlaceholderConfigurer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
		
	private static Map<String, Object> properties = new HashMap<String, Object>();
	

//	@Override
//    protected void loadProperties(final Properties props) throws IOException {
//        super.loadProperties(props);
//        for (final Object key : props.keySet()) {
//            properties.put((String) key, props.getProperty((String) key));
//        }
//    }

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
	     super.processProperties(beanFactory, props);
	 
	     properties = new HashMap<String, Object>();
	     for (Object key : props.keySet()) {
	         String keyStr = key.toString();
	         properties.put(keyStr, props.getProperty(keyStr));
	    }
	     if (LOGGER.isDebugEnabled()) {
	    	 LOGGER.debug("Loader Properties :: {}", properties.toString());
	     }
	 }
	
	

    public static String getString(final String name) {
        return String.valueOf(properties.get(name));
    }
	
    public static int getInt(final String name) {
    	return Integer.parseInt(String.valueOf(properties.get(name)));
    }

    public static long getLong(final String name) {
    	return Long.parseLong(String.valueOf(properties.get(name)));
    }

    
    private void onChange() {

    }
}
