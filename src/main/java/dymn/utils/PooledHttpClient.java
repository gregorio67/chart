package dymn.utils;

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
import org.springframework.util.ResourceUtils;

public class PooledHttpClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateBean.class);
	
	private CloseableHttpClient httpClient;

	private static final String[] _sslProtocols = {"TLSv1.1", "TLSv1.2"};
	private String[] sslProtocols;
	private boolean ignoreCertValidate;
	private String keyStoreLoc;
	private String trustStoreLoc;
	private String password;
	private int maxPool;
	
	public PooledHttpClient() {
		if (ignoreCertValidate) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Ignore Cetitification validation");
			}
			
			try {
				httpClient = noCertValidate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Cetitification validation");
			}
			try {
				httpClient = certValidate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private CloseableHttpClient noCertValidate() throws Exception {
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
        return httpClient;
	}

	/**
	 * keystore : 
	 *     -- Create keystore : keytool -genkey -alias replserver -keyalg RSA -keystore keystore.jks
	 *     -- Add private key to keystore  keytool -certreq -alias [openapi.kyobo.co.kr] -file [certrequest.crt] -keypass  [password] -keystore [keystore.jks] -storepass [password]
	 *     -- Create truststore with certificate : keytool -import -trustcacerts -alias [replserver] -file [client.crt]  -keystore [truststore.ts] -storepass [password] -noprompt
	 * @return
	 * @throws Exception
	 */
	private CloseableHttpClient certValidate() throws Exception {
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
        return httpClient;
	}

	public void setSslProtocols(String[] sslProtocols) {
		if (sslProtocols == null) {
			this.sslProtocols = _sslProtocols;
		}
		else {
			this.sslProtocols = sslProtocols;			
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
		this.maxPool = maxPool;
	}
}
