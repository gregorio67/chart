package dymn.chart.wsock.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

 
//	private SimpMessagingTemplate messagingTemplate;
//	
//	@Autowired
//	public MessageServiceImpl(SimpMessagingTemplate messagingTemplate) {
//		this.messagingTemplate = messagingTemplate;
//	}
	
	public void pushMessage() throws Exception {
//		messageTemplate = new SimpMessagingTemplate();
//        messagingTemplate.setMessageConverter(new StringMessageConverter());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HHmmSS");
        String date = sdf.format(new Date());
        String result = date + " :: This message was sent from server !!!";
        resultMap.put("result", result);
        
        
//        messagingTemplate.convertAndSend("/topic/greetings", JsonUtil.object2Json(resultMap));

	}

}
