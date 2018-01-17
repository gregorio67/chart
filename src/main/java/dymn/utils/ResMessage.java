package cmn.util.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmn.util.base.BaseConstants;
import cmn.util.spring.MessageUtil;


public class ResMessage {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResMessage.class);
	

	/**
	 * 
	 *<pre>
	 * Generate Response Message
	 *</pre>
	 * @param msgCode String
	 * @param isError boolean
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> makeResponse(String msgCode, boolean isError) throws Exception {
		return makeResponse(null, msgCode, null, isError);
	}

	/**
	 *  Generate Response Message
	 *<pre>
	 *
	 *</pre>
	 * @param msgCode 
	 * @param param
	 * @param isError
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> makeResponse(String msgCode, Object[] param, boolean isError) throws Exception {
		return makeResponse(null, msgCode, param, isError);
	}

		
	public static Map<String, Object> makeResponse(Map<String, Object> dataMap, String msgCode, boolean isError) throws Exception {
		return makeResponse(dataMap, msgCode, null, isError);
	}

	/**
	 * 
	 *<pre>
	 * Generate Response Message for JSON
	 *</pre>
	 * @param dataMap Map<String, Object> return data
	 * @param msgCode String message code
	 * @param param Object[] message parameter
	 * @param isError boolean error or not
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> makeResponse(Map<String, Object> dataMap, String msgCode, Object[] param, boolean isError) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = makeMessage(msgCode, param, isError);
		if (isError) {
			resultMap.put(BaseConstants.RESPONSE_BODY_MESSAGE, msgMap);
		}
		else {
			resultMap.put(BaseConstants.RESPONSE_BODY_MESSAGE, msgMap);
			resultMap.put(BaseConstants.RESPONSE_BODY_DATA, dataMap);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Response :: {}", resultMap.toString());
		}
		return resultMap;
	}

	

	/**
	 * 
	 *<pre>
	 * Generate Response Message
	 *</pre>
	 * @param msgCode
	 * @param message
	 * @param isError
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> makeResponseWithMessage(String msgCode, String message, boolean isError) throws Exception {
		return makeResponseWithMessage(null, msgCode, message, isError);
	}
	
	/**
	 * 
	 *<pre>
	 * Generate Response Message
	 *</pre>
	 * @param dataMap Map<String, Object>
	 * @param msgCode String
	 * @param message Sting 
	 * @param isError boolean
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> makeResponseWithMessage(Map<String, Object> dataMap, String msgCode, String message, boolean isError) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = makeMessage(msgCode, message, isError);
		if (isError) {
			resultMap.put(BaseConstants.RESPONSE_BODY_MESSAGE, msgMap);
		}
		else {
			resultMap.put(BaseConstants.RESPONSE_BODY_MESSAGE, msgMap);
			resultMap.put(BaseConstants.RESPONSE_BODY_DATA, dataMap);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Response :: {}", resultMap.toString());
		}
		return resultMap;
	}
	
	
	/**
	 *
	 *<pre>
	 * Generate Message
	 *</pre>
	 * @param msgCode String message code
	 * @param param Object[] message parameter
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> makeMessage(String msgCode, Object[] param, boolean isError) throws Exception {
		Map<String, Object> msgMap = new HashMap<String, Object>();

		String message = MessageUtil.getMessage(msgCode, param);
		msgMap.put(BaseConstants.RESPONSE_MESSAGE_NAME, message);
		msgMap.put(BaseConstants.RESPONSE_MESSAGE_CODE, msgCode);
		if (isError) {
			msgMap.put(BaseConstants.RESPONSE_STATUS_MESSAGE_CODE, BaseConstants.STATUS_ERROR);
		}
		else {
			msgMap.put(BaseConstants.RESPONSE_STATUS_MESSAGE_CODE, BaseConstants.STATUS_SUCCESS);
		}

		return msgMap;
	}

	/**
	 * 
	 *<pre>
	 * Generate message
	 *</pre>
	 * @param msgCode
	 * @param message
	 * @param isError
	 * @return
	 * @throws Exception
	 */
	
	public static Map<String, Object> makeMessage(String msgCode, String message, boolean isError) throws Exception {
		Map<String, Object> msgMap = new HashMap<String, Object>();

		msgMap.put(BaseConstants.RESPONSE_MESSAGE_NAME, message);
		msgMap.put(BaseConstants.RESPONSE_MESSAGE_CODE, msgCode);
		if (isError) {
			msgMap.put(BaseConstants.RESPONSE_STATUS_MESSAGE_CODE, BaseConstants.STATUS_ERROR);
		}
		else {
			msgMap.put(BaseConstants.RESPONSE_STATUS_MESSAGE_CODE, BaseConstants.STATUS_SUCCESS);
		}

		return msgMap;
	}

}
