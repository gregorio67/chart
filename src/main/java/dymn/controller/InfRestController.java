package sehati.inf.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sehati.inf.service.ApiService;
import sehati.inf.service.RestClient;
import sehati.inf.service.WsInterfaceService;
import sehati.inf.vo.request.WeatherReqVo;
import sehati.inf.vo.request.WeatherSearchReqVo;
import sehati.inf.vo.response.WeatherResVo;
import sehati.inf.vo.response.WeatherSearchResVo;
import sehati.util.common.NullUtil;
import sehati.util.common.RandomUtil;
import sehati.util.converter.MapUtil;

@RestController

public class InfRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfRestController.class);

	private static final String REQUEST_PARAM_BODY = "reqData";
	private static final String REQUEST_PARAM_HEADER = "reqHeader";

	@Resource(name="apiService")
	private ApiService apiService;

	@Resource(name="restClient")
	private RestClient restClient;

	@Resource(name="wsInterfaceService")
	private WsInterfaceService wsInterfaceService;

	@Resource(name="messageSource")
	private MessageSource messageSource;

	@RequestMapping(value="/api/{systemId}/{infId}/{serviceName}", method={RequestMethod.GET, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> serviceCall(@RequestBody Map<String, Object> params, @PathVariable String systemId, @PathVariable String infId, @PathVariable String serviceName) throws Exception {

		if (NullUtil.isNull(systemId) || NullUtil.isNull(infId) || NullUtil.isNull(serviceName)) {
			return makeResponse(null, "err.service.uri", null, false);
		}

		/** Check client ID **/
		@SuppressWarnings("unchecked")
		Map<String, Object> keyMap = (Map<String, Object>) params.get(REQUEST_PARAM_HEADER);
		try {
			if (apiService.selectAuthCode(keyMap) < 1) {
				return makeResponse(null, "err.service.authcode", null, false);
			}
		}
		catch(Exception ex) {
			return makeResponse(null, "err.service.database", null, false);
		}


		@SuppressWarnings("unchecked")
		Map<String, Object> param = (Map<String, Object>) params.get(REQUEST_PARAM_BODY);

		/** Call Restful Service **/
		String intfGroupId = systemId + "." + infId;
		LOGGER.info("Rest Service call start :: {}::{}", systemId + infId + serviceName, param.toString());
		Map<String, Object> restResult = null;
		try {
			restResult = restClient.exchange(intfGroupId, serviceName, null, MediaType.APPLICATION_JSON,null, param);
		}
		catch(Exception ex) {
			return makeResponse(null, "err.service.apicall", null, true);
		}

		LOGGER.info("Rest Service Result :: {} :: {}", systemId + infId + serviceName, restResult.toString());

		return makeResponse(restResult, "info.service.success", null, false);
	}


	@RequestMapping(value="/api/authcode/{userId}", method={RequestMethod.GET, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getClientId(@RequestBody Map<String, Object> params, @PathVariable String userId) throws Exception {

		if (NullUtil.isNull(userId)) {
			return makeResponse(null, "inf.err.service.uri", null, true);
		}

		Map<String, Object> dataMap = new HashMap<String, Object>();
		/** Create client ID **/
		dataMap.put("clientId", userId);
		dataMap.put("authCode", RandomUtil.getUUID());
		dataMap.put("authTime", System.currentTimeMillis());


		try {
			if (apiService.updateApiUser(dataMap) < 1 ) {
				return makeResponse(null, "inf.err.service.uri", null, true);
			}
			else {
				return makeResponse(dataMap, "inf.service.success", null, false);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return makeResponse(null, "err.service.uri", null, true);
		}
	}

	@RequestMapping(value="/api/webservice/{infId}/{serviceName}", method={RequestMethod.GET, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> callWebService(@RequestBody Map<String, Object> params, @PathVariable String infId, @PathVariable String serviceName) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		/** Convert Map to ApiUser **/
		WeatherReqVo weatherReqVo = new WeatherReqVo();
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = (Map<String, Object>) params.get(REQUEST_PARAM_BODY);
		weatherReqVo = (WeatherReqVo) MapUtil.convertMapToObject(paramMap, weatherReqVo);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Result :: {} :: {}", weatherReqVo.getCityName(), weatherReqVo.getCountryName());
		}

		WeatherResVo weatherResVo = wsInterfaceService.requestWsIf(infId, serviceName, weatherReqVo, WeatherResVo.class, null);

		WeatherSearchReqVo weatherSearchReqVo = new WeatherSearchReqVo();
		weatherSearchReqVo.setCountryName("Korea");

		WeatherSearchResVo weatherSearchResVo = wsInterfaceService.requestWsIf(infId, serviceName, weatherSearchReqVo, WeatherSearchResVo.class, null);



		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("weatherResVo :: {}", weatherResVo.getGetWeatherResult());
			LOGGER.info("weatherSearchResVo :: {}", weatherSearchResVo.getGetCitiesByCountryResult());
		}
		return resultMap;
	}

	/**
	 *
	 *<pre>
	 * Create Error Message
	 *</pre>
	 * @param msgCode String
	 * @param param Object[]
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> makeMessage(String msgCode, Object[] param, boolean isError) throws Exception {
		Map<String, Object> errMap = new HashMap<String, Object>();

		String message = messageSource.getMessage(msgCode, param, Locale.getDefault());
		errMap.put("msg", message);
		if (isError) {
			errMap.put("status", "ERROR");
		}
		else {
			errMap.put("status", "SUCCESS");
		}

		return errMap;
	}

	private Map<String, Object> makeResponse(Map<String, Object> dataMap, String msgCode, Object[] param, boolean isError) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = makeMessage(msgCode, param, isError);
		if (isError) {
			resultMap.put("message", msgMap);
		}
		else {
			resultMap.put("message", msgMap);
			resultMap.put("response", dataMap);
		}
		return resultMap;
	}
}
