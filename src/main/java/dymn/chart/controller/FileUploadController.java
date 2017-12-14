package dymn.chart.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dymn.utils.ExcelReadOption;

@Controller
public class FileUploadController {

	@RequestMapping(value="uploadView.do")
	public @ResponseBody ModelAndView uploadView() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("uploadFile");

		return mav;
	}
	
	@RequestMapping(value="upload.do")
	public @ResponseBody Map<String, Object> uploadFile(@RequestParam("files") MultipartFile[] files) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (files == null) {
			resultMap.put("statusCode", 99);
			return resultMap;
		}
		
		for(MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("D:/temp/" + file.getOriginalFilename());
            Files.write(path, bytes);
		}
		
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath("destFile.getAbsolutePath()");
        excelReadOption.setOutputColumns("A","B","C","D","E","F");
        excelReadOption.setStartRow(2);
		
		return resultMap;
	}
}
