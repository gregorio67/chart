package sehati.inf.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
//import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
//import javax.xml.namespace.QName;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageException;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.WebServiceTransportException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;

import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapVersion;
//import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.support.MarshallingUtils;

import sehati.inf.cmn.util.Constants.InterfaceLogStatus;
import sehati.inf.service.WsInterfaceService;
import sehati.util.spring.BeanUtil;



@Service( "wsInterfaceService" )
public class WsInterfaceServiceImpl implements WsInterfaceService {

	/**
	 * 통합 I/F 전용 Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger( "intfLogger" );

	private static final String NON_PROCESS_TIME = "-1";

	public static final String PROCESS_TIME = "X-IF-Process-Time";

	public static final String PREFERRED_PREFIX = "soap12";

	/**
	 * Spring Message Source
	 */
	@Resource( name = "messageSource" )
	private MessageSource messageSource;

	/**
	 * WebService Template
	 */
	@Resource( name = "webServiceTemplate" )
	private WebServiceTemplate webServiceTemplate;

	/* (non-Javadoc)
	 * @see com.lgcns.ems.mo.common.intf.ws.WsInterfaceService#requestWsIf(java.lang.String, java.lang.String, java.lang.Object, java.lang.Class)
	 */
	public <V, T> T requestWsIf( final String intfGroupId, String interfaceId, final V requestVo, Class<T> responseType, final String command) {
		Assert.hasLength( intfGroupId );
		Assert.hasLength( interfaceId );

		// Response Entity
		ResponseEntity<T> responseEntity = null;

		// Exception (외부 연동 수행 결과 나올 수 있는 예외 객체를 따로 저장한다)
		Exception resultEx = null;

		try {
			// Get Interface Specification
			Map<String, String> interfaceMap = BeanUtil.getBean(intfGroupId);
			String interfaceSpec = interfaceMap != null ? interfaceMap.get( interfaceId ) : null;
			Assert.hasLength( interfaceSpec, "Invalid WebService Client Argument: interfaceSpec" );

			// 외부 WebService 연동 host를 조회
			Assert.isTrue( interfaceMap.containsKey( "host" ), "Invalid WebService Client Argument: host" );

			String host = interfaceMap.get( "host" );
//			String host = tmpHost.startsWith( "http://" ) || tmpHost.startsWith( "https://" ) ? tmpHost : commonConfig.getProperty( tmpHost );
			Assert.hasLength( host, "Invalid WebService Client Argument: host" );

			// Request Body Check
			Assert.notNull( requestVo, "Invalid WebService Client Argument: requestVo" );

			// Make URL & Method
			String url = new StringBuilder( host ).append( interfaceSpec ).toString();

			// RIA Custom: 요청 Body에 대한 암호화를 위해 별도 처리 Flag를 표시
//			if ( "RIA_WS_IF".equals( intfGroupId ) ) {
//				RequestContextHolder.getRequestAttributes().setAttribute( "intfGroupId", intfGroupId, RequestAttributes.SCOPE_REQUEST );
//			}

			// Exchange request/response
			responseEntity = webServiceTemplate.sendAndReceive( url, new WebServiceMessageCallback() {
				public void doWithMessage( WebServiceMessage message ) throws IOException, TransformerException {
					SaajSoapMessage saajSoapMessage = (SaajSoapMessage)message;
					SoapVersion	soapVersion = saajSoapMessage.getVersion();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("SOAP VERSION :: {}", soapVersion);
					}
				    Map<String, String> interfaceMap = null;
			    	try {
						interfaceMap = BeanUtil.getBean( intfGroupId );
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();

				    	String prefix = interfaceMap.get("prefix") != null ? interfaceMap.get("prefix") : "";
						SOAPPart soapPart = soapMessage.getSOAPPart();
			    		SOAPEnvelope envelope = soapPart.getEnvelope();
			    		SOAPHeader soapHeader = envelope.getHeader();

				    	if (prefix != null) {
//							soapMessage.getSOAPHeader().detachNode();
				    		SOAPBody body = soapMessage.getSOAPBody();
				    		envelope.removeNamespaceDeclaration(envelope.getPrefix());
				    		envelope.setPrefix(prefix);
				    		body.setPrefix(prefix);
				    		soapHeader.setPrefix(prefix);
				    	}
				    	/** Add Soap Action **/
				    	if (soapVersion == SoapVersion.SOAP_11) {
				    		if (interfaceMap.get("soapAction") == null) {
				    			throw new RuntimeException("");
				    		}
						    String soapAction = interfaceMap.get("soapAction");
						    ((SaajSoapMessage)message).setSoapAction(soapAction);
				            /**
				             * Set SOAP Action in Soap message header
				             */
//				            QName wsaActionQName = new QName("http://www.w3.org/2005/08/addressing", "Action", "wsa");
//				            SoapHeaderElement wsaAction =  (SoapHeaderElement) soapHeader.addHeaderElement(wsaActionQName);
//				            wsaAction.setText(soapAction);
				    	}
					}
					catch(SOAPException se) {
						LOGGER.error("Message : {}", se.getMessage());
						throw new RuntimeException(se.getMessage());
					}
					catch(Exception ex) {
						LOGGER.error("Message : {}", ex.getMessage());
						throw new RuntimeException(ex.getMessage());
					}

					Marshaller marshaller = webServiceTemplate.getMarshaller();
					if ( marshaller == null ) {
						throw new IllegalStateException( "No marshaller registered. Check configuration of WebServiceTemplate." );
					}

                   MarshallingUtils.marshal( marshaller, requestVo, message );

                    /**
                     * Logging sending message to RIA
                     */
                    SaajSoapMessage reqMessage = (SaajSoapMessage) message;
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    reqMessage.writeTo(out);
                    String msg = new String(out.toByteArray());
                    LOGGER.info("Request Message  :: {}", msg);

				}

			}, new WebServiceMessageExtractor<ResponseEntity<T>>() {

				/* (non-Javadoc)
				 * @see org.springframework.ws.client.core.WebServiceMessageExtractor#extractData(org.springframework.ws.WebServiceMessage)
				 */
				@SuppressWarnings( "unchecked" )
				public ResponseEntity<T> extractData( WebServiceMessage message ) throws IOException, TransformerException {
					MimeHeaders header = ((SaajSoapMessage)message).getSaajMessage().getMimeHeaders();

					// 'TimestampClientInterceptor' 클래스에서 응답시간을 기록한 'X-IF-Process-Time' 헤더를 가져온다
					HttpHeaders responseHeader = new HttpHeaders();
					String[] timestampHeader = header.getHeader( PROCESS_TIME );
					if ( timestampHeader != null && timestampHeader.length > 0 ) {
						responseHeader.set( PROCESS_TIME, timestampHeader[0] );
					}

					Unmarshaller unmarshaller = webServiceTemplate.getUnmarshaller();
					if ( unmarshaller == null ) {
						throw new IllegalStateException( "No unmarshaller registered. Check configuration of WebServiceTemplate." );
					}

					/**
					 * Logging received message from RIA
					 */
                    SaajSoapMessage respMessage = (SaajSoapMessage) message;
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    respMessage.writeTo(out);
                    String msg = new String(out.toByteArray());
                    LOGGER.info("Response Message :: {}", msg);

	                T responseVo = (T)MarshallingUtils.unmarshal( unmarshaller, (SaajSoapMessage)message );

					return new ResponseEntity<T>( responseVo, responseHeader, HttpStatus.OK );
				}
			} );

			// Return Response Body
			return responseEntity.getBody();
		} catch ( Exception exception ) {
			resultEx = exception;
//			String message = messageSource.getMessage("err.com.interface", null, LocaleContextHolder.getLocale());
			throw new RuntimeException( exception );
		} finally {
			logWsIfHistory( intfGroupId, interfaceId, responseEntity, resultEx );
		}
	}

	/**
	 * Logging REST I/F History
	 *
	 * @param intfGroupId Interface Group Id (context-interface-ws.xml 'util:map' bean의 id)
	 * @param interfaceId Interface Code (context-interface-ws.xml 'intfGroupCode' bean에 해당하는 map의 key)
	 * @param responseEntity Response Entity 객체 (200 OK Only, 정상응답이면 null을 부여할 수 없다)
	 * @param resultEx 연동 후 발생한 예외 클래스
	 * @param <T> Response Body Class Type
	 */
	private <T> void logWsIfHistory( String intfGroupId, String interfaceId, ResponseEntity<T> responseEntity, Exception resultEx ) {
		InterfaceLogStatus resultCode = null;
		String processTime = responseEntity != null && responseEntity.getHeaders().containsKey( PROCESS_TIME ) ? responseEntity.getHeaders().getFirst( PROCESS_TIME )
				: NON_PROCESS_TIME;
		String exMsg = resultEx != null ? resultEx.getMessage() : null;

		// 결과 Exception 유/뮤 및 예외 타입에 따른 로깅 정보 설정
		if ( resultEx == null ) {
			resultCode = InterfaceLogStatus.SUCCESS;
		} else if ( resultEx instanceof WebServiceClientException ) {
			if ( resultEx instanceof WebServiceTransportException ) {
				// Exception thrown when an HTTP 4xx is received.
				resultCode = InterfaceLogStatus.CLIENT_ERROR;
			} else if ( resultEx instanceof WebServiceIOException ) {
				// Exception thrown when an I/O error occurs.
				resultCode = InterfaceLogStatus.IO_ERROR;
			} else {
				resultCode = InterfaceLogStatus.CLIENT_ERROR;
			}
		} else if ( resultEx instanceof WebServiceMessageException ) {
			// Exception thrown when an HTTP 5xx is received.
			resultCode = InterfaceLogStatus.SERVER_ERROR;
		} else if ( resultEx instanceof WebServiceException ) {
			// Base class for exceptions thrown by WebServiceTemplate
			resultCode = InterfaceLogStatus.ETC_ERROR;
		} else {
			// Unknown Error
			resultCode = InterfaceLogStatus.UNKNOWN;
		}

		LOGGER.info( "WS,{},{},{},{},{},{},{}", intfGroupId, interfaceId, resultCode, processTime, "", StringUtils.defaultString( exMsg ), "" );
	}
}
