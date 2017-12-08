package dymn.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;


/**
 * key
 * @author KB099
 *
 */
public class RestTemplateBean implements InitializingBean{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateBean.class);
	
	private static final String SEPERATOR = "\\|";
	
	private static final String[] _sslProtocols = {"TLSv1.1", "TLSv1.2"};
	private String[] sslProtocols;
	private boolean ignoreCertValidate;
	private String keyStoreLoc;
	private String trustStoreLoc;
	private String password;
	private int maxPool;
	private int connectionRequestTimeout;
	private int connectTimeout;
	private int readTimeout;
	
	private static RestTemplate restTemplate;
	
	public void init() throws Exception {
		
		if (ignoreCertValidate) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Ignore Cetitification validation");
			}
			noCertValidate();
		}
		else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Cetitification validation");
			}
			certValidate();
		}				
    }
	
	
	private void noCertValidate() throws Exception {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		/** This is not validate certificate **/
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext,
        		sslProtocols,
                null, 
                NoopHostnameVerifier.INSTANCE);
        
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();
        
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(maxPool);
        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();
        
        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restTemplate = new RestTemplate(requestFactory);
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setHttpClient(httpClient);
        restTemplate.setMessageConverters(getMessageConverters());
	}

	/**
	 * keystore : 
	 *     -- Create keystore : keytool -genkey -alias replserver -keyalg RSA -keystore keystore.jks
	 *     -- Add private key to keystore  keytool -certreq -alias [openapi.kyobo.co.kr] -file [certrequest.crt] -keypass  [password] -keystore [keystore.jks] -storepass [password]
	 *     -- Create truststore with certificate : keytool -import -trustcacerts -alias [replserver] -file [client.crt]  -keystore [truststore.ts] -storepass [password] -noprompt
	 * @return
	 * @throws Exception
	 */
	private void certValidate() throws Exception {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();

		
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(ResourceUtils.getFile(keyStoreLoc), password.toCharArray(), password.toCharArray())
                .loadTrustMaterial(ResourceUtils.getFile(trustStoreLoc), password.toCharArray())
                .build();
		
		/** This is not validate certificate **/
//		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		keyStore.load(new FileInputStream(new File(keyStoreLoc)), password.toCharArray());
//      SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy())
//                .loadKeyMaterial(keyStore, "password".toCharArray()).build();
        
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext,
        		sslProtocols,
                null, 
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();
        
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(maxPool);

        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();
        
        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        
        restTemplate = new RestTemplate(requestFactory);
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setHttpClient(httpClient);
        restTemplate.setMessageConverters(getMessageConverters());
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
		String uri = beanMap.get(interfaceId).split(SEPERATOR)[0];
		String url = host + uri;
		HttpMethod method = HttpMethod.valueOf(beanMap.get(interfaceId).split(SEPERATOR)[1]);
		
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
	public Map<String, Object> exchange(String interfaceGroupId, String interfaceId, Map<String, String> extraHeaders, MediaType contentType, Map<String, String> parameter, Map<String, Object> requestBody) {
		
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
		Map<String, String> beanMap = (Map<String, String>) ApplicationContextProvider.getApplicationContext().getBean(interfaceGroupId);
		
		String host = beanMap.get("host");
		String tempUri = beanMap.get(interfaceId);
		String uri = tempUri.split(SEPERATOR)[0];
		String url = host + uri;
		HttpMethod method = HttpMethod.valueOf(tempUri.split(SEPERATOR)[1]);
		
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
    
	
	public void setSslProtocols(String[] sslProtocols) {
		this.sslProtocols = sslProtocols;
		if (sslProtocols == null) {
			this.sslProtocols = _sslProtocols;
		}
	}

	public void setIgnoreCertValidate(boolean ignoreCertValidate) {
		this.ignoreCertValidate = ignoreCertValidate;
	}

	public void setKeyStoreLoc(String keyStoreLoc) {
		this.keyStoreLoc = keyStoreLoc;
	}

	public void setTrustStoreLoc(String trustStoreLoc) {
		this.trustStoreLoc = trustStoreLoc;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMaxPool(int maxPool) {
		if (maxPool == 0) {
			this.maxPool = 100;
		}
		else {
			this.maxPool = maxPool;			
		}
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		if (connectionRequestTimeout == 0) {
			this.connectionRequestTimeout= 5000;
		}
		else {
			this.connectionRequestTimeout = connectionRequestTimeout;			
		}
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
		if (!this.ignoreCertValidate) {
			if (this.keyStoreLoc == null || this.trustStoreLoc == null || this.password == null) {
				throw new RuntimeException("Check KeyStore, TrustStore and Password");
			}
		}
		
	}
}
