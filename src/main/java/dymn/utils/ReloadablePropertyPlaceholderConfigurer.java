package dymn.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.StringValueResolver;

public class ReloadablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadablePropertyPlaceholderConfigurer.class);
	private ReloadablePlaceholderResolvingStringValueResolver reloadableValueResolver;
	private static Map<String, Object> properties = new HashMap<String, Object>();

	public void reloadProperties() throws IOException {
		Properties props = mergeProperties();
		this.reloadableValueResolver.refreshProperties(props);

		properties.clear();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			properties.put(keyStr, props.getProperty(keyStr));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Loader Properties :: {}", properties.toString());
		}
		
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		this.reloadableValueResolver = new ReloadablePlaceholderResolvingStringValueResolver(props);
		StringValueResolver valueResolver = this.reloadableValueResolver;
		this.doProcessProperties(beanFactoryToProcess, valueResolver);
		properties = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			properties.put(keyStr, props.getProperty(keyStr));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Loader Properties :: {}", properties.toString());
		}		
	}

	private class ReloadablePlaceholderResolvingStringValueResolver implements StringValueResolver {

		private final PropertyPlaceholderHelper helper;
		private final ReloadablePropertyPlaceholderConfigurerResolver resolver;

		public ReloadablePlaceholderResolvingStringValueResolver(Properties props) {
			this.helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix, valueSeparator,
					ignoreUnresolvablePlaceholders);
			this.resolver = new ReloadablePropertyPlaceholderConfigurerResolver(props);
		}

		public String resolveStringValue(String strVal) throws BeansException {
			String value = this.helper.replacePlaceholders(strVal, this.resolver);
			return (value.equals(nullValue) ? null : value);
		}

		private void refreshProperties(Properties props) {
			this.resolver.setProps(props);
		}
	}

	private class ReloadablePropertyPlaceholderConfigurerResolver implements PlaceholderResolver {

		private Properties props;

		private ReloadablePropertyPlaceholderConfigurerResolver(Properties props) {
			this.props = props;
		}

		public String resolvePlaceholder(String placeholderName) {
			return ReloadablePropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName, props,
					SYSTEM_PROPERTIES_MODE_FALLBACK);
		}

		public void setProps(Properties props) {
			this.props = props;
		}
	}
	
	public static String getString(String key) {
		return String.valueOf(properties.get(key));
	}
	public static int getInt(String key) {
		return Integer.parseInt(String.valueOf(properties.get(key)));
		
	}
	public static long getLong(String key) {
		return Long.parseLong(String.valueOf(properties.get(key)));
		
	}
	
}
