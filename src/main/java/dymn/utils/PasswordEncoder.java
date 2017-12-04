package dymn.utils;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder extends BCryptPasswordEncoder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoder.class);
	
	private boolean base64;
	
	public PasswordEncoder(int strength) {
		super(strength);
	}

	@Override
	public String encode(CharSequence rawPassword) {
		String encodedText = super.encode(rawPassword);
		if (base64) {
			return Base64.encodeBase64String(encodedText.getBytes());
		}
		else {
			return encodedText;
		}
	}
	
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (base64) {
			String base64 = new String(Base64.decodeBase64(encodedPassword));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}:{}", rawPassword, base64);
			}
			return super.matches(rawPassword, base64);
		}
		else {
			return super.matches(rawPassword, encodedPassword);
		}
	}

	public void setBase64(boolean base64) {
		this.base64 = base64;
	}
	
	
}
