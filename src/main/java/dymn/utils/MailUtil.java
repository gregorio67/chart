package dymn.utils;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	private JavaMailSenderImpl mailSender;
	
	/**
	 * send main with simple message
	 * @param recipientAddress
	 * @param subject
	 * @param message
	 * @throws Exception
	 */
	public void sendMail(String recipientAddress, String subject, String message) throws Exception {
		
		SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        
        mailSender.send(email);
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("----------------------------------------------------");
        	LOGGER.debug("{} :: {} :: {}", recipientAddress, subject, message);
        	LOGGER.debug("Successfully mail sent");
        	LOGGER.debug("----------------------------------------------------");
        }
	}
	
	/**
	 * Send Mail with mime type
	 * @param fromAddr
	 * @param recipientAddress
	 * @param subject
	 * @param body
	 * @param filePath
	 * @throws Exception
	 */
	public void sendMail(final String fromAddr, final String recipientAddress, String subject, final String body, final String filePath) throws Exception {
	
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		mimeMessage.setContent(body, "text/html");
		helper.setTo(fromAddr);
		helper.setSubject(subject);
		helper.setFrom(recipientAddress);
		if (filePath != null) {
			int idx = filePath.lastIndexOf("/");
			String fileName = filePath.substring(idx+1);
			helper.addAttachment(fileName, new File(filePath));
		}
		mailSender.send(mimeMessage);
		
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("----------------------------------------------------");
        	LOGGER.debug("{} :: {} :: {}", recipientAddress, subject, body);
        	LOGGER.debug("Successfully mail sent");
        	LOGGER.debug("----------------------------------------------------");
        }
		
		
//		mailSender.send(new MimeMessagePreparator() {
//			  public void prepare(MimeMessage mimeMessage) throws MessagingException, javax.mail.MessagingException {
//			    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//			    message.setFrom(fromAddr);
//			    message.setTo(recipientAddress);
//			    message.setSubject("A file for you");
//			    message.setText(body, true);
//			    message.addAttachment("CoolStuff.doc", new File(filePath));
//			  }
//			});
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
}
