package dymn.utils;

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
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateConfig {
	
	private static final String[] _sslProtocols = {"TLSv1.1", "TLSv1.2"};
	private String[] sslProtocols;
	private boolean ignoreCertValidate;
	private String keyStore;
	private String trustStore;
	private String password;
	private static RestTemplate restTemplate;
	
	public RestTemplate restTemplate() throws Exception {
		
		if (restTemplate == null) {
			synchronized(this) {
				if (ignoreCertValidate) {
					return noCertValidate();
				}
				else {
					return certValidate();
				}				
			}
		}
		else 
			return restTemplate;
    }
	
	private RestTemplate noCertValidate() throws Exception {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		/** This is not validate certificate **/
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext,
        		sslProtocols,
                null, 
                NoopHostnameVerifier.INSTANCE);
//                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();
        
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();
        
        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setHttpClient(httpClient);
        return restTemplate;
	}

	private RestTemplate certValidate() throws Exception {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		/** This is not validate certificate **/
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext,
        		sslProtocols,
                null, 
                NoopHostnameVerifier.INSTANCE);
//                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();
        
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.build();
        
        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setHttpClient(httpClient);
        return restTemplate;
	}
	
	
//	public <T> T exchange(String url, String method, T param) throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		
//		HttpEntity<T> entity = new HttpEntity<T>(param, headers);
//		
//		ResponseEntity<T> response = restTemplate.exchange(url, method, entity, T, param);		
//	}
	
	
	public void setSslProtocols(String[] sslProtocols) {
		this.sslProtocols = sslProtocols;
		if (sslProtocols == null) {
			this.sslProtocols = _sslProtocols;
		}
	}

	public void setIgnoreCertValidate(boolean ignoreCertValidate) {
		this.ignoreCertValidate = ignoreCertValidate;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
