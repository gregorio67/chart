package dymn.chart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dymn.utils.SftpUtil;

@Controller
public class ChartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChartController.class);
	
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="app")
	private Properties app;
	
	
	@Resource(name="system")
	private Properties system;
	
	@Resource(name="sftpClient")
	private SftpUtil sftpClient;
	
//	@Resource(name="jsonView")
//	private MappingJackson2JsonView jsonView;
	
	@RequestMapping(value="chart.do")
	public ModelAndView showChart() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("c3chart");
		Locale.setDefault(new Locale("en"));
		String msg = messageSource.getMessage("welcome.springmvc", null, Locale.getDefault());
		
		LOGGER.debug(system.getProperty("aa.rest.server.ip"));
		LOGGER.debug(app.getProperty("aa.default.locale"));
		LOGGER.debug(msg);
		return mav;
	}
	
	
	@RequestMapping(value="showData.do")
	public @ResponseBody Map<String, List<Integer>> showData() throws Exception {
		
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(200);
		list1.add(200);
		list1.add(400);
		result.put("2014-01-01", list1);

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(100);
		list2.add(150);
		list2.add(350);
		result.put("2014-01-02", list2);

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(70);
		list3.add(50);
		list3.add(120);
		result.put("2014-01-03", list3);

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(500);
		list4.add(300);
		list4.add(800);
		result.put("2014-01-04", list4);

//		
//		
//		Map<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("date", "2014-01-01");
//		map1.put("upload", "200");
//		map1.put("download", "200");
//		map1.put("total", "400");
//		
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("date", "2014-01-01");
//		map2.put("upload", "200");
//		map2.put("download", "200");
//		map2.put("total", "400");
//
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("date", "2014-01-01");
//		map3.put("upload", "200");
//		map3.put("download", "200");
//		map3.put("total", "400");
//
//		Map<String, Object> map4 = new HashMap<String, Object>();
//		map4.put("date", "2014-01-01");
//		map4.put("upload", "200");
//		map4.put("download", "200");
//		map4.put("total", "400");
//		
//		result.add(map1);
//		result.add(map2);
//		result.add(map3);
//		result.add(map4);
		
		return result;
	}
	
	@RequestMapping(value="websocket.do")
	public String wsView() throws Exception {
		return "wsclient";
	}
	
	@RequestMapping(value="angular.do")
	public String viewAngular() throws Exception {
		return "angular";
	}
	
	@RequestMapping(value="country.do")
	public @ResponseBody List<Map<String, Object>>country(@RequestBody Map<String, Object> param) throws Exception {
//	public @ResponseBody List<Map<String, Object>>country() throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map0 = new HashMap<String, Object>();
		map0.put("Name", "NAME");
		map0.put("Country", "COUNTRY");

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("Name", "KKIMDOY");
		map1.put("Country", "KOREA");
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("Name", "KIMDOY");
		map2.put("Country", "US");
		
		resultList.add(map0);
		resultList.add(map1);
		resultList.add(map2);
		
		return resultList;
	}
	
	@RequestMapping(value="sftp.do")
	public @ResponseBody Map<String, Object> getRemoteFile() throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		byte[] bytes = sftpClient.getFile("/logs/jeus/DepInst11P/JeusServer.log", "D:/temp/JeusServer.log");
		resultMap.put("log", new String(bytes));
		return resultMap;
	}

}
