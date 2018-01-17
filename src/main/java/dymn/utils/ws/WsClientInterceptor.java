package cmn.util.spring.ws;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

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

import cmn.util.common.NullUtil;
import cmn.util.common.SoapMessagDump;
import cmn.util.exception.BizException;


public class WSClientInterceptor implements ClientInterceptor, InitializingBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WSClientInterceptor.class);
	
	private boolean enableSecurity;
	
	private String actor;
	
	private boolean mustUnderstand;
	
	private String wsuId;
	
	private String username;
	
	private String password;
	
	private String prefix;
	
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

		/**
		 * Need to check In bound and out bound 
		 */
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
					soapEnvelope.setPrefix(WsConstants.DEFAULT_SOAP11_PREFIX);
					soapHeader.setPrefix(WsConstants.DEFAULT_SOAP11_PREFIX);
					soapBody.setPrefix(WsConstants.DEFAULT_SOAP11_PREFIX);
				}
				else {
					soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
					soapEnvelope.setPrefix(WsConstants.DEFAULT_SOAP12_PREFIX);
					soapHeader.setPrefix(WsConstants.DEFAULT_SOAP12_PREFIX);
					soapBody.setPrefix(WsConstants.DEFAULT_SOAP12_PREFIX);
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
			
			/** SOAP Message Dump **/
			SoapMessagDump.dumpSoapMessage(soapMessage);

		} catch (Exception e) {
			throw new BizException(e.getMessage());
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
		QName security = new QName(WsConstants.DEFAULT_WSSE_NAMESPACE, "Security", WsConstants.DEFAULT_WSSE_PREFIX);
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

		/** Create UsernameToken Queue Name **/
		QName usernameToken = new QName(WsConstants.DEFAULT_WSSE_NAMESPACE, WsConstants.DEFAULT_WSSE_USERNAMETOKEN_TAG, WsConstants.DEFAULT_WSSE_PREFIX);
		SOAPHeaderElement eUsemameToken = soapHeader.addHeaderElement(usernameToken);
		
		/** Create User name element **/
		QName qUserName = new QName(WsConstants.DEFAULT_WSSE_NAMESPACE, WsConstants.DEFAULT_WSSE_USERNAME_TAG, WsConstants.DEFAULT_WSSE_PREFIX);
		SOAPHeaderElement eUserName = soapHeader.addHeaderElement(qUserName);
		
		if (!NullUtil.isNull(wsuId)) {
			/** Add name space to user name with wsu **/
			eUserName.addNamespaceDeclaration("wsu", WsConstants.DEFAULT_WSSE_WSU_NAMESPACE);
			eUserName.setAttribute("wsu:Id", wsuId);
			eUserName.addTextNode(username);
		}
		
		/** Create Password element **/
		QName qPassword = new QName(WsConstants.DEFAULT_WSSE_NAMESPACE, WsConstants.DEFAULT_WSSE_PASSWORD_TAG, WsConstants.DEFAULT_WSSE_PREFIX);
		SOAPHeaderElement ePassword = soapHeader.addHeaderElement(qPassword);
		ePassword.setAttribute("Type", WsConstants.DEFAULT_WSSE_PASSWORD_NAMESPACE);
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
				LOGGER.error("You shoud set username and password");
				throw new BizException("You shoud set username and password");
			}
		}
	}
}
