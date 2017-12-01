package dymn.chart.rest.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

	public static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
	
	/** This is defined context-util.xml **/
	@Resource(name="restserver")
	private Map<String, String> restServer; 
	
	@RequestMapping(value="/getConsumer", method={RequestMethod.GET, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getConsumer(@RequestBody Map<String, Object> param) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Iterator<String> itr = restServer.keySet().iterator();
		while(itr.hasNext()) {
			String key = itr.next();
			resultMap.put(key, restServer.get(key));
		}
		return resultMap;
	}
}
