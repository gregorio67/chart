package dymn.chart.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dymn.chart.service.FormatService;

public class NotifyHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotifyHandler.class);
	
	private long sinceId = 0;
	
	@Resource(name="formatService")
	private FormatService formatService;
	
	private void suspend(final AtmosphereResource resource) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
	        resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
	            @Override
	            public void onSuspend(AtmosphereResourceEvent event) {
	                countDownLatch.countDown();
	                resource.removeEventListener(this);
	            }
	        });
	        resource.suspend();
	        try {
	            countDownLatch.await();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
    	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws Exception {
		LOGGER.info("Welcome home! the client locale is "+ locale.toString());
		model.addAttribute("serverTime", formatService.formatDate(locale) );
		return "home";
	}
	
	@RequestMapping(value="/twitter/concurrency")
	@ResponseBody
	public void twitterAsync(AtmosphereResource atmosphereResource){
		final ObjectMapper mapper = new ObjectMapper();
	
		this.suspend(atmosphereResource);

	        final Broadcaster bc = atmosphereResource.getBroadcaster();
	        
	        LOGGER.info("Atmo Resource Size: " + bc.getAtmosphereResources().size());
	
	        
	        bc.scheduleFixedBroadcast(new Callable<String>() {
	            
	            //@Override	
	            public String call() throws Exception {
	            	List<Map<String, Object>> clientMessage = new ArrayList<Map<String, Object>>();
	                return mapper.writeValueAsString(clientMessage);
	            }
	
	        }, 10, TimeUnit.SECONDS);
	}
	
}
