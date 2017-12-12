package dymn.chart.wsock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PushController {
	
//    private SimpMessagingTemplate template;
//
//    
//    @Autowired
//    public PushController(SimpMessagingTemplate template) {
//        this.template = template;
//    }
	
   @RequestMapping(path="/push")
   public void pushMessage() throws Exception {
//	   template.convertAndSend("/topic/greetings","Hello !!!");
   }
   
}
