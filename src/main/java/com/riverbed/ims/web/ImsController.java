package com.riverbed.ims.web;

import java.util.List;
import java.util.LinkedHashMap;

import com.riverbed.ims.MethodA;
import com.riverbed.ims.model.Tier;
import com.riverbed.ims.model.Method;
import com.riverbed.ims.model.MethodResult;
import com.riverbed.ims.model.CallResult;;

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
	public CallResult processTier(@RequestBody Tier thisTier, final HttpServletRequest request) throws Exception {
		logger.debug(String.format("Got request for: %s\n%s", request.getRequestURI(), thisTier));

		CallResult callResult = new CallResult();

		for (Method method : thisTier.getMethod()) {
			Integer mode = method.getMode();
			Integer min = method.getMin();
			Integer max = method.getMax();
			String message = method.getMessage();
			MethodA thisMethodA = new ByteBuddy()
  			.subclass(MethodA.class)
				.name("com.riverbed.ims." + method.getName())
  			.make()
				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded()
				.getDeclaredConstructor()
				.newInstance();

			logger.debug(String.format("Calling method... Name:%s Mode:%d Min:%d Max:%d", thisMethodA.getClass().getName(), mode, min, max));
			callResult.addResult(thisMethodA.process(mode, min, max, message));
		}

		if (thisTier.getCall() != null){
			for (LinkedHashMap call : thisTier.getCall()){
				logger.debug(String.format("Making request to: %s with body:\n%s", call.get("url"), call));
				callResult.setCall(restTemplate.postForObject(call.get("url").toString(), call, Object.class));
				logger.info(String.format("Result: %s", restTemplate.postForObject(call.get("url").toString(), call, Object.class)));
			}
		} else{
			logger.debug("No external calls to make...");
		}

		return callResult;
	}
}
