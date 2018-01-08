package sehati.inf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Splitter;

import sehati.util.spring.ApplicationContextProvider;
import sehati.util.spring.BeanUtil;

/**
 * @Project : SEHATI Project
 * @Class : Rest.java
 * @Description :
 * @Author : LGCNS
 * @Since : 2017. 12. 21.
 *
 * @Copyright (c) 2017 SEHATI All rights reserved.
 *----------------------------------------------------------
 * Modification Information
 *----------------------------------------------------------
 * 날짜            수정자             변경사유
 *----------------------------------------------------------
 *2017. 12. 21.          LGCNS           최초작성
 *----------------------------------------------------------
 */
public class RestClient implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

	@Resource(name="restHttpClient")
	private CloseableHttpClient restHttpClient;

	private MappingJackson2HttpMessageConverter messageConverter;
	private int connectTimeout;
	private int readTimeout;



    private static final Splitter IF_SPEC_SPLITTER = Splitter.on( '|' );

	private static RestTemplate restTemplate;

	public void init() throws Exception {

		LOGGER.info("RestClient building satrt");
		HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restTemplate = new RestTemplate(requestFactory);
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setHttpClient(restHttpClient);
//        restTemplate.setMessageConverters(getMessageConverters());
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(messageConverter);
        restTemplate.setMessageConverters(messageConverters);

        LOGGER.info("RestClient building end");
    }

	/**
	 *
	 * @param interfaceGroupId
	 * @param interfaceId
	 * @param extraHeaders
	 * @param contentType
	 * @param parameter
	 * @param requestBody
	 * @param responseType
	 * @return
	 * @throws Exception
	 */
	public <V, T> T exchange(String interfaceGroupId, String interfaceId, Map<String, String> extraHeaders, MediaType contentType, Map<String, String> parameter, V requestBody, Class<T> responseType) throws Exception {

		ResponseEntity<T> responseEntity = null;

		HttpHeaders headers = new HttpHeaders();
		if (extraHeaders != null) {
			headers.setAll(extraHeaders);
		}

		if (requestBody != null) {
			headers.setContentType(contentType == null ? MediaType.APPLICATION_JSON : contentType);
		}
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<V> entity = new HttpEntity<V>(requestBody, headers);

		@SuppressWarnings("unchecked")
		Map<String, String> beanMap = (Map<String, String>) ApplicationContextProvider.getApplicationContext().getBean(interfaceGroupId);

		String host = beanMap.get("host");
		String uri = IF_SPEC_SPLITTER.splitToList(beanMap.get(interfaceId)).get(0);
		String url = host + uri;
		HttpMethod method = HttpMethod.valueOf(IF_SPEC_SPLITTER.splitToList(beanMap.get(interfaceId)).get(1));

		Map<String, Object> parameters = new HashMap<String, Object>();
		if (parameter != null && !parameter.isEmpty()) {
			parameters.putAll(parameter);
		}

		responseEntity = restTemplate.exchange(url,  method, entity, responseType, parameters);

		T responseBody = responseEntity.getBody();

		return responseBody;
	}

	/**
	 * RestTemplate exchange
	 * @param interfaceGroupId
	 * @param interfaceId
	 * @param extraHeaders
	 * @param contentType
	 * @param parameter
	 * @param requestBody
	 * @return
	 */
	public Map<String, Object> exchange(String interfaceGroupId, String interfaceId, Map<String, String> extraHeaders, MediaType contentType, Map<String, String> parameter, Map<String, Object> requestBody) throws Exception {

		ResponseEntity<HashMap<String, Object>> responseEntity = null;

		HttpHeaders headers = new HttpHeaders();
		if (extraHeaders != null) {
			headers.setAll(extraHeaders);
		}
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		if (requestBody != null) {
			headers.setContentType(contentType == null ? MediaType.APPLICATION_JSON : contentType);
		}
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(requestBody, headers);

		@SuppressWarnings("unchecked")
		Map<String, String> beanMap = (Map<String, String>) BeanUtil.getBean(interfaceGroupId);

		String host = beanMap.get("host");
		String uri = IF_SPEC_SPLITTER.splitToList(beanMap.get(interfaceId)).get(0);
		String url = host + uri;
		HttpMethod method = HttpMethod.valueOf(IF_SPEC_SPLITTER.splitToList(beanMap.get(interfaceId)).get(1));

		Map<String, Object> parameters = new HashMap<String, Object>();
		if (parameter != null && !parameter.isEmpty()) {
			parameters.putAll(parameter);
		}

		ParameterizedTypeReference<HashMap<String, Object>> responseType = new ParameterizedTypeReference<HashMap<String, Object>>() {};

		responseEntity = restTemplate.exchange(url,  method, entity, responseType, parameters);

		Map<String, Object> responseBody = responseEntity.getBody();

		return responseBody;

	}

	/**
	 * Message Converter
	 * @return
	 */
    private static List<HttpMessageConverter<?>> getMessageConverters() {
    	 List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
         //Add the Jackson Message converter
    	 MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		// Note: here we are making this converter to process any kind of response,
		// not only application/*json, which is the default behaviour
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
		messageConverters.add(converter);
		return messageConverters;
	}

	public void setConnectTimeout(int connectTimeout) {
		if (connectTimeout == 0) {
			this.connectTimeout = 5000;
		}
		else {
			this.connectTimeout = connectTimeout;
		}
	}

	public void setReadTimeout(int readTimeout) {
		if (readTimeout == 0) {
			this.readTimeout = 5000;
		}
		else {
			this.readTimeout = readTimeout;
		}
	}

	public void afterPropertiesSet() throws Exception {
	}
}
