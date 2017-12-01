package dymn.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class CustomSslSocketFactory {

	
	private boolean ignoreCertValidation;
	private String[] sslProtocols;
	private String trustStore;
	private String keyStore;
	
	private static final String[] _sslProtocols = {"TLSv1,1, TLSv1.2"};
	
	public CustomSslSocketFactory() {
		
	}
	
	
	/**
	 * Create SSLSocketConnection factory.
	 * @return
	 * @throws Exception
	 */
	public SSLConnectionSocketFactory create() throws Exception {
 		if (ignoreCertValidation) {
			return noCertValiate();
		}
		else {
			return certValiate();
		}
	}

	/**
	 * This method does not validate certificate
	 * @return
	 * @throws Exception
	 */
	private SSLConnectionSocketFactory noCertValiate() throws Exception {
		
        SSLContext sslcontext = SSLContexts.custom()
//              .loadTrustMaterial(new File("my.keystore"), "nopassword".toCharArray(), new TrustSelfSignedStrategy())
//              .loadTrustMaterial(null, new TrustSelfSignedStrategy())
              .loadTrustMaterial(new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
              	
              })
              .build();
      
	      // Allow TLSv1 protocol only
	     SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	              sslcontext,
	              sslProtocols,
	              null, 
	              NoopHostnameVerifier.INSTANCE);
	//              SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	//              SSLConnectionSocketFactory.getDefaultHostnameVerifier());
	     return sslsf;
	}
	
	/**
	 * This method validate certificate
	 * @return
	 * @throws Exception
	 */
	private SSLConnectionSocketFactory certValiate() throws Exception {
		
        SSLContext sslcontext = SSLContexts.custom()
//              .loadTrustMaterial(new File("my.keystore"), "nopassword".toCharArray(), new TrustSelfSignedStrategy())
//              .loadTrustMaterial(null, new TrustSelfSignedStrategy())
              .loadTrustMaterial(new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
              	
              })
              .build();
      
	      // Allow TLSv1 protocol only
	     SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	              sslcontext,
	              sslProtocols,
	              null, 
	              NoopHostnameVerifier.INSTANCE);
	//              SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	//              SSLConnectionSocketFactory.getDefaultHostnameVerifier());
	     return sslsf;
	}
	
	public void setIgnoreCertValidation(boolean ignoreCertValidation) {
		this.ignoreCertValidation = ignoreCertValidation;
		if (!ignoreCertValidation) {
			if (trustStore == null || keyStore == null) {
				throw new RuntimeException("Truststore or Keystore is not defined in your config xml file");
			}
		}
	}

	public void setSslProtocols(String[] sslProtocols) {
		if (sslProtocols == null) {
			sslProtocols = _sslProtocols;
		}
		this.sslProtocols = sslProtocols;
	}
	
	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}


	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}
}
