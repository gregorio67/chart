package dymn.chart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dymn.utils.CryptotUtil;
import dymn.utils.PasswordEncoder;

@Controller
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@Resource(name="passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Resource(name="crypto")
	private CryptotUtil crypto;
	
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
	
	@RequestMapping(value="password.do")
	public @ResponseBody Map<String, Object> checkPassword() throws Exception {
		
		String encText = passwordEncoder.encode("kyobo11!");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("encText", encText);
		
		boolean isMatch = passwordEncoder.matches("kyobo11!", encText);
		
		resultMap.put("isMatch", isMatch);
		return resultMap;
		
	} 
	
	@RequestMapping(value="crypto.do")
	public @ResponseBody Map<String, Object> checkCrypto() throws Exception {
		
		String encText = crypto.encrypt("kyobo111!");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("encText", encText);
		
		String plainText = crypto.decrypt(encText);
		
		resultMap.put("plainText", plainText);

		return resultMap;
		
	} 
}
