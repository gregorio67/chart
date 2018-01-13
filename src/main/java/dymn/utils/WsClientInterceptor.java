package sehati.inf.cmn.util;

import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.AbstractSoapMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import sehati.util.common.NullUtil;

/**
 * @Project : SEHATI Project
 * @Class : WSInterceptor.java
 * @Description :
 * @Author : LGCNS
 * @Since : 2017. 12. 27.
 *
 * @Copyright (c) 2017 SEHATI All rights reserved.
 *----------------------------------------------------------
 * Modification Information
 *----------------------------------------------------------
 * 날짜            수정자             변경사유
 *----------------------------------------------------------
 *2017. 12. 27.          LGCNS           최초작성
 *----------------------------------------------------------
 */
/**
 * @author LGCNS
 *
 */
public class WSClientInterceptor implements ClientInterceptor, InitializingBean  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WSClientInterceptor.class);

	private static final String wsPrefix = "wsse";
	private static final String wsseNS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String passwordType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
	private static final String wsuNS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	
	private boolean enableSecurity;
	
	private String actor;
	
	private boolean mustUnderstand;
	
	private String wsuId;
	
	private String username;
	
	private String password;
	
	private String prefix;
	
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

		WebServiceMessage requestMessage = messageContext.getRequest();
		AbstractSoapMessage abstractSaajMessage = (AbstractSoapMessage) requestMessage;
		SaajSoapMessage saajSoapMessage = (SaajSoapMessage) abstractSaajMessage;
		SoapVersion	soapVersion = saajSoapMessage.getVersion();
		
		SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();
//		SOAPMessage soapMessage = ((SOAPMessageContext) messageContext).getMessage();
		try {
			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
			SOAPHeader soapHeader = soapEnvelope.getHeader();
			SOAPBody soapBody = soapEnvelope.getBody();

			
			/** Add Security Header with user name token**/
			if (enableSecurity) {
				addUsernameTokenSecurityHeader(soapHeader);				
			}
			
			/** Change prefix **/
			if (NullUtil.isNull(prefix)) {
				if (soapVersion == SoapVersion.SOAP_11) {
					soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
					soapEnvelope.setPrefix(prefix);
					soapEnvelope.setPrefix("soap");
					soapHeader.setPrefix("soap");
					soapBody.setPrefix("soap");
				}
				else {
					soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
					soapEnvelope.setPrefix("soap12");
					soapHeader.setPrefix("soap12");
					soapBody.setPrefix("soap12");
				}
			}
			else {
				soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
				soapEnvelope.setPrefix(prefix);
				soapHeader.setPrefix(prefix);
				soapBody.setPrefix(prefix);
			}
			
			/** Changed message save **/
			soapMessage.saveChanges();
			
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        soapMessage.writeTo(out);
	        String msg = new String(out.toByteArray());
	        LOGGER.info("Security Request Message  :: {}", msg);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		return true;
	}
	
	/**
	 * 
	 *<pre>
	 * Add Security Header with UsernameToken
	 *</pre>
	 * @param soapMessage SOAPMessage
	 * @throws Exception
	 */
	protected void addUsernameTokenSecurityHeader(SOAPHeader soapHeader ) throws Exception {


		/** Create Security Header **/
		QName security = new QName(wsseNS, "Security", wsPrefix);
		SOAPHeaderElement eSecurity = soapHeader.addHeaderElement(security);
		
		/** Set actor **/
		if (!NullUtil.isNull(actor)) {
			eSecurity.setAttribute("actor", actor);
//			eSecurity.setActor(actor);			
		}
		/** Set must understand flag **/
		if (!NullUtil.isNull(mustUnderstand)) {
			eSecurity.setAttribute("mustUnderstand", "1");
//			eSecurity.setMustUnderstand(mustUnderstand);
		}

		QName usernameToken = new QName(wsseNS, "UsernameToken", wsPrefix);
		SOAPHeaderElement eUsemameToken = soapHeader.addHeaderElement(usernameToken);
		
		/** Create User name element **/
		QName qUserName = new QName(wsseNS, "UserName", wsPrefix);
		SOAPHeaderElement eUserName = soapHeader.addHeaderElement(qUserName);
		
		if (!NullUtil.isNull(wsuId)) {
			/** Add name space to user name with wsu **/
			eUserName.addNamespaceDeclaration("wsu", wsuNS);
			eUserName.setAttribute("wsu:Id", wsuId);
			eUserName.addTextNode(username);			
		}
		
		/** Create Password element **/
		QName qPassword = new QName(wsseNS, "Password", wsPrefix);
		SOAPHeaderElement ePassword = soapHeader.addHeaderElement(qPassword);
		ePassword.setAttribute("Type", passwordType);
		ePassword.addTextNode(password);


		/** Add Child element to UsernameToken element **/
		eUsemameToken.addChildElement(eUserName);
		eUsemameToken.addChildElement(ePassword);
		
		/** Add Child element to Security element **/
		eSecurity.addChildElement(eUsemameToken);
		
	}

	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		return false;
	}

	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		return false;
	}

	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

	}

	
	public void setEnableSecurity(boolean enableSecurity) {
		this.enableSecurity = enableSecurity;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public void setMustUnderstand(boolean mustUnderstand) {
		this.mustUnderstand = mustUnderstand;
	}

	public void setWsuId(String wsuId) {
		this.wsuId = wsuId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void afterPropertiesSet() throws Exception {
		if (enableSecurity) {
			if (NullUtil.isNull(username) || NullUtil.isNull(password)) {
				throw new RuntimeException("You shoud set username and password");
			}
		}
	}
}
