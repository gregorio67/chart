package dymn.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class BeanUtil {

	public static <T> T getBean(String beanName) throws Exception {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		
		@SuppressWarnings("unchecked")
		T bean = (T) context.getBean(beanName);
		
		return bean;
	}
}
