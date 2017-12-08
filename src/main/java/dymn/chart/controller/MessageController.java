package dymn.chart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessageController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public @ResponseBody Map<String, Object> greeting(@RequestBody Map<String, Object> message) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        String result = "Hello, " + message.get("name") + "!!!";
        resultMap.put("result", result);
    	return message;
   }
    
   @RequestMapping(value="wsstomp.do")
   public ModelAndView websocketview() throws Exception {
	   ModelAndView mav = new ModelAndView();
	   mav.setViewName("wsstomp");
	   
	   return mav;
   }
}
