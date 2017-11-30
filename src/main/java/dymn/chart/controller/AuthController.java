package dymn.chart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@RequestMapping(value="/login.do")
	public ModelAndView login(@RequestParam(value="error", required=false) String error,
							  @RequestParam(value="logout", required=false) String logout) throws Exception {
	
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("error :: {}, logout :: {}", error, logout);
		}
		ModelAndView mav = new ModelAndView();
		if (error != null) {
			mav.addObject("error", "Invalid username and password!");
		}
		
		if (logout != null) {
			mav.addObject("msg", "Successfully logouted");
		}
		
		mav.setViewName("login");
		
		return mav;
	}
}
