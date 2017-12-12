package dymn.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import com.sun.istack.internal.NotNull;

public class CacheUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtil.class);

	private Cache cache;
	
	@NotNull
	private EhCacheCacheManager cacheManager;
	
	@NotNull
	private String cacheName;
		
	public void init() throws Exception {
		cache = this.cacheManager.getCache(cacheName);
	}
	
	public <V> void putData(String key, V data) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			printData(data);
		}
		cache.put(key, data);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData(String key, Class<T> clazz) throws Exception {
		ValueWrapper value = cache.get(key);
		if (LOGGER.isDebugEnabled()) {
			printData(value.get());
		}
		
		return (T) value.get();
	}
	
	private <V> void printData(V data) throws Exception {
		
		if (data instanceof Map) {
			LOGGER.debug("Put Data :: {}", data.toString());
		}
		else if (data instanceof List){
			LOGGER.debug("Put Data :: {}", ((List<?>) data).toArray().toString());
		}
		else {
			LOGGER.debug("Put Data :: {}", data);
		}

	}

	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

}
