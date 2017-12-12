package dymn.chart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dymn.chart.service.ApiService;
import dymn.utils.CacheUtil;
import dymn.utils.CryptotUtil;
import dymn.utils.PasswordEncoder;
import dymn.utils.RandomUtil;
import dymn.utils.ReloadPropertyUtil;
import dymn.utils.ReloadablePropertyPlaceholderConfigurer;


@Controller
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@Resource(name="passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Resource(name="crypto")
	private CryptotUtil crypto;
	
	@Resource(name="propertiesConfiguration")
	private PropertiesConfiguration propertiesConfiguration;
	
	@Resource(name="cacheUtil")
	private CacheUtil cacheUtil;
	
	@Resource(name="apiService")
	private ApiService apiService;

//	@Resource(name="propertiesUtil")
//	private ReloadablePropertyPlaceholderConfigurer propertiesUtil;
//	
//	@Resource(name="reloadingStrategy")
//	private ReloadingStrategy reloadingStrategy;
	
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
		
//		if(reloadingStrategy.reloadingRequired()) {
//			propertiesConfiguration.refresh();
//		}
//		LOGGER.debug("test:: {}", propertiesConfiguration.getProperty("test.reload"));

		LOGGER.debug("test:: {}", ReloadablePropertyPlaceholderConfigurer.getString("test.reload"));
		
		LOGGER.debug("test1:: {}", propertiesConfiguration.getProperty("test.reload"));

		LOGGER.debug("test1:: {}", ReloadPropertyUtil.getString("test.reload"));

		return resultMap;
		
	} 
	
	@RequestMapping(value="getAuthCode.do")
	public @ResponseBody Map<String, Object> getAuthCode() throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("clientId", "kkimdoy");
		resultMap.put("authTime", System.currentTimeMillis());
		resultMap.put("authCode", RandomUtil.getUUID());
		
		cacheUtil.putData("kkimdoy", resultMap);
		apiService.updateApiUser(resultMap);
		
		return resultMap;
		
	}
	
	@RequestMapping(value="checkAuthCode.do")
	public @ResponseBody Map<String, Object> checkAuthCode() throws Exception {
		long refreshTime = 60000;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("clientId", "kkimdoy");
		
		Map<String, Object> apiResult = apiService.selectClientId(param);
		LOGGER.debug("api :: {}", apiResult);
		
		Map<String, Object> resultMap = cacheUtil.getData("kkimdoy", Map.class);
		
		long authTime = Long.parseLong(String.valueOf(resultMap.get("authTime")));
		if ((authTime + refreshTime) < System.currentTimeMillis()) {
			resultMap.put("code", "Your auth code was expired");
		}
		return resultMap;
		
	}
	
}
