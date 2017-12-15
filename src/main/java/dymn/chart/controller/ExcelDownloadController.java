package dymn.chart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dymn.chart.service.StudentService;

@Controller
public class ExcelDownloadController {

	@Resource(name="studentService")
	private StudentService studentService;
	
	
	@RequestMapping(value="exceldownload.do")
	public ModelAndView excelDownload() throws Exception {
		ModelAndView mav = new ModelAndView("XlsView");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("sheetName", "Student");
		String[] heaerNames = {"학생번호", "학생명", "부서코드", "생년월일"};
		header.put("headerNames", heaerNames);
		header.put("fileName", "students.xls");
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> studentList = studentService.selectStudents(param);
		
		resultMap.put("header", header);
		resultMap.put("data", studentList);
		
		mav.addObject("excelData", resultMap);
		
		return mav;
	}
}
