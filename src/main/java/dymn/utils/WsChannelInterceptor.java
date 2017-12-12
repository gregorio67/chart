package dymn.utils;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class WsChannelInterceptor extends ChannelInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WsChannelInterceptor.class);

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
	    if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
	        Principal userPrincipal = headerAccessor.getUser();
	        if(!validateSubscription(userPrincipal, headerAccessor.getDestination()))
	        {
	            throw new IllegalArgumentException("No permission for this topic");
	        }
	    }
	    
	    MessageHeaders headers = message.getHeaders();
	    SimpMessageType type = (SimpMessageType) headers.get("simpMessageType");
	    String simpSessionId = (String) headers.get("simpSessionId");

	    if (type == SimpMessageType.CONNECT) {
	        Principal principal = (Principal) headers.get("simpUser");
	        LOGGER.debug("WsSession " + simpSessionId + " is connected for user " + principal.getName());
	    } else if (type == SimpMessageType.DISCONNECT) {
	        LOGGER.debug("WsSession " + simpSessionId + " is disconnected");
	    }
	    return message;
	}
	
	
	private boolean validateSubscription(Principal principal, String topicDestination) {
	    if (principal == null) {
	        // unauthenticated user
	        return false;
	    }
	    LOGGER.debug("Validate subscription for {} to topic {}",principal.getName(),topicDestination);
	    //Additional validation logic coming here
	    return true;
	}
}
