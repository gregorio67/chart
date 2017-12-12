package dymn.chart.rest.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dymn.utils.WADLGenUtil;

@Controller
@RequestMapping("application.wadl")
public class WADLController {
 
//	@Resource(name="wadlGenUtil")
//	private WADLGenUtil wadlGenUtil;
	
//    @RequestMapping(method=RequestMethod.GET, produces={"application/xml"} ) 
//    public @ResponseBody Application generateWadl(HttpServletRequest request) {
//    	return wadlGenUtil.generateWadl(request);
//    }
}
