package dymn.chart.websocket;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
    
	  @Override
	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	        sessionList.add(session);
	        LOGGER.info("{} �����", session.getId()); 
	        
	    }
	    
	    /**
	     * Ŭ���̾�Ʈ�� ������ ������ �޽����� �������� �� ����Ǵ� �޼ҵ�
	     */
	    @Override
	    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	        
	    	LOGGER.info("{}�� ���� {} ����", session.getId(), message.getPayload());
	        for(WebSocketSession sess : sessionList){
	            sess.sendMessage(new TextMessage("echo:" + message.getPayload()));
	        }
	        
	    }
	    
	    /**
	     * Ŭ���̾�Ʈ ������ ������ �� ����Ǵ� �޼ҵ�
	     */
	    @Override
	    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	        sessionList.remove(session);	        
	        LOGGER.info("{} ���� ����.", session.getId());
	    }
}
