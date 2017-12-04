package dymn.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PropertyReloadInterceptor extends HandlerInterceptorAdapter {

//	@Resource(name="propertiesConfiguration")
//	private PropertiesConfiguration propertiesConfiguration;
//	
//	@Resource(name="reloadingStrategy")
//	private ReloadingStrategy reloadingStrategy;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		if(reloadingStrategy.reloadingRequired()) {
//			propertiesConfiguration.refresh();
//		}
		
		return true;
	}
}
