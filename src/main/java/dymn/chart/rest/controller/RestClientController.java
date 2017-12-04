package dymn.chart.rest.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class RestClientController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientController.class);
	
//	@Resource(name = "restTemplate")
//	private RestTemplate restTemplate;
	
	@RequestMapping(value="restclient.do")
	public @ResponseBody Map<String, Object> restClient() throws Exception {
		
		Map<String, Object> resultMap = null;
		
		return resultMap;
	}
}
