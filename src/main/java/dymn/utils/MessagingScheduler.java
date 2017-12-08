package dymn.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

public class MessagingScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MessagingScheduler.class);

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate; 

//    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }

    /**
     * minute, hour, day of month, month and day of week. e.g. "0 * * * * MON-FRI"
     */
//    @Scheduled(fixedRate=5000)     //5초마다 수행하도록 설정
    public void checkNotice(){
        logger.info("checkNotice call");
        try{
//            messagingTemplate.setMessageConverter(new StringMessageConverter());
            Map<String, Object> resultMap = new HashMap<String, Object>();
            
            String result = " This message was sent from server !!!";
            resultMap.put("result", result);
//            messagingTemplate.convertAndSend("/topic/greetings", resultMap);

        }catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }
}
