package dymn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;


public class MessagingScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingScheduler.class);

    private SimpMessagingTemplate messagingTemplate;	
    
//    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    public MessagingScheduler(SimpMessagingTemplate messagingTemplate) {  
        this.messagingTemplate = messagingTemplate;
//        this.setMessagingTemplate(messageTemplate);
    } 
    

    /**
     * minute, hour, day of month, month and day of week. e.g. "0 * * * * MON-FRI"
     */
//    @Scheduled(fixedRate=5000)     //5초마다 수행하도록 설정
    public void checkNotice(){
    	LOGGER.info("checkNotice call");
        try{
            messagingTemplate.setMessageConverter(new StringMessageConverter());
            Map<String, Object> resultMap = new HashMap<String, Object>();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HHmmSS");
            String date = sdf.format(new Date());
            String result = date + " :: This message was sent from server !!!";
            resultMap.put("result", result);
            
            
            messagingTemplate.convertAndSend("/topic/greetings", JsonUtil.object2Json(resultMap));

        }catch(Exception ex){
        	LOGGER.error(ex.getMessage());
        }
    }
}
