package dymn.utils;

import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.sun.istack.internal.NotNull;

/**
 * 
 * @author KB099
 * When Java Security: Illegal key size or default parameters error occurs.
 * Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8 Download
 * Extract the jar files from the zip and save them in ${java.home}/jre/lib/security/.
 */
public class CryptotUtil implements InitializingBean{

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptotUtil.class);
	
	private static SecretKeyFactory factory = null;
	private static KeySpec keySpec = null;
	private static SecretKey secretKey = null;
	private static Cipher cipher = null;
	
	@NotNull
	private String keyAlgorithm;
	
	@NotNull
	private String ciperAlgorithm;
	
	@NotNull
	private String secureAlgorithm;
	
	@NotNull
	private String password ;
	
	@NotNull
	private String salt;
	

	public String encrypt(String text) throws Exception {
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(text.getBytes("UTF-8"));
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("{}::{}", text, cipherText);
        }
        
        return Base64.encodeBase64String(cipherText);
	}
	
	
	public String decrypt(String encText) throws Exception {

		byte[] decodedText = Base64.decodeBase64(encText.getBytes());
		AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        
        String plainText = new String(cipher.doFinal(decodedText), "UTF-8");
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("{}::{}",new String(decodedText), plainText );
        }
        
        return plainText;
	}
	
	public void afterPropertiesSet() throws Exception {
		factory =  SecretKeyFactory.getInstance(keyAlgorithm);
		keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey tmp = factory.generateSecret(keySpec);
	    secretKey = new SecretKeySpec(tmp.getEncoded(), secureAlgorithm);
	    cipher = Cipher.getInstance(ciperAlgorithm);
	}

	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
	}

	public void setCiperAlgorithm(String ciperAlgorithm) {
		this.ciperAlgorithm = ciperAlgorithm;
	}

	public void setSecureAlgorithm(String secureAlgorithm) {
		this.secureAlgorithm = secureAlgorithm;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}
