package dymn.chart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChartController.class);
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
//	@Resource(name="jsonView")
//	private MappingJackson2JsonView jsonView;
	
	@RequestMapping(value="chart.do")
	public ModelAndView showChart() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("c3chart");
		Locale.setDefault(new Locale("en"));
		String msg = messageSource.getMessage("welcome.springmvc", null, Locale.getDefault());
		
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
}
