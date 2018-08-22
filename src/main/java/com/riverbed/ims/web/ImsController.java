package com.riverbed.ims.web;

import java.util.LinkedHashMap;

import com.riverbed.ims.service.MethodA;
import com.riverbed.ims.model.Tier;
import com.riverbed.ims.model.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.client.RestTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

@RestController
public class ImsController {

	//@Autowired
	//private MethodA methodA;

	private static Log logger = LogFactory.getLog(ImsController.class);

	private RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/**")
	@ResponseBody
	public String generateLoad() {
		String output = "Welcome to IMS (the Infinite Monkey Service)!"
			+ "A Java based transaction/load generator. "
			+ "Please use a POST request with a transaction JSON as body.";
		return output;
	}

	@PostMapping("/**")
	@ResponseBody
	public String processTier(@RequestBody Tier thisTier, final HttpServletRequest request) throws Exception {
		logger.info(String.format("Got request for: %s\n%s", request.getRequestURI(), thisTier));

		for (Method method : thisTier.getMethod()) {
			Integer mode = method.getMode();
			Integer min = method.getMin();
			Integer max = method.getMax();
			MethodA thisMethodA = new ByteBuddy()
  			.subclass(MethodA.class)
				.name("com.riverbed.ims.service." + method.getName())
  			.make()
				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded()
				.getDeclaredConstructor()
				.newInstance();

			logger.debug(String.format("Calling method... Name:%s Mode:%d Min:%d Max:%d", thisMethodA.getClass().getName(), mode, min, max));
			String output = thisMethodA.process(mode, min, max);
			logger.debug(output);
		}

		if (thisTier.getCall() != null){
			for (LinkedHashMap call : thisTier.getCall()){
				logger.info(String.format("Making request to: %s", call.get("url")));
				logger.debug(String.format("with body:\n%s", call));
				String response = restTemplate.postForObject(call.get("url").toString(), call, String.class);
				logger.info(response);
			}
		} else{
			logger.info("No external calls to make...");
		}

		//return String.format("Done! Response:\n%s", response);
		return "Done!";
	}
}
