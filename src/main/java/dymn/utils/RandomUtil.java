package dymn.utils;

import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);
	
	private static String lowStr = "abcdefghijklmnopqrstuvwxyz";
	private static String upStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String numStr = "1234567890";
	private static String specialStr = "!@#$%^&*(){}[]";
	
	public static String generatePassword(int len) throws Exception {
		
//		RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
//				.withinRange('0', 'z')
//                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
//                .build();
//		
//		String result = randomStringGenerator.generate(len);
		
		StringBuilder sb = new StringBuilder();
		String pwd = RandomStringUtils.random(2, lowStr);
		sb.append(pwd);
		
		pwd = RandomStringUtils.random(2, upStr);
		sb.append(pwd);

		pwd = RandomStringUtils.random(2, numStr);
		sb.append(pwd);

		pwd = RandomStringUtils.random(2, specialStr);
		sb.append(pwd);
		
		String result = sb.toString();

		return result;
	}
	
	
	public static String getUUID() throws Exception {
		String uuid = String.valueOf(UUID.randomUUID());
		uuid = uuid.replaceAll("-", "");
		return uuid;
	}

}
