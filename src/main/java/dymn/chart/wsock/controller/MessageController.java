package dymn.chart.wsock.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dymn.chart.wsock.service.MessageService;


@Controller
public class MessageController {

	@Resource(name="messageService")
	private MessageService messageService;
	
	
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public @ResponseBody Map<String, Object> greeting(@RequestBody Map<String, Object> message) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        String result = "Hello, " + message.get("name") + "!!!";
        resultMap.put("result", result);
    	return resultMap;
   }
    
   @RequestMapping(value="wsstomp.do")
   public ModelAndView websocketview() throws Exception {
	   ModelAndView mav = new ModelAndView();
	   mav.setViewName("wsstomp");
	   
	   return mav;
   }
}
