package co.web;

import java.util.List;
import java.util.LinkedHashMap;

import co.MethodA;
import co.model.Tier;
import co.model.Method;
import co.model.MethodResult;
import co.model.CallResult;
import co.service.HandleDb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.client.RestTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

@Controller
public class ImsController {

	private static Log logger = LogFactory.getLog(ImsController.class);

	private RestTemplate restTemplate = new RestTemplate();

	private HandleDb handleDb = new HandleDb();

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String testDb() {
		return handleDb.execute("bla", 70);
	}

	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public String generateLoad() {
		return "intro";
	}

	@RequestMapping(value = "/**", method = RequestMethod.POST, headers = "x-Html=yes" ,consumes = {
        "application/JSON",
        "application/XML"
    })
	public String processTierHtml(@RequestBody Tier thisTier, final HttpServletRequest request, Model model) throws Exception {
		logger.debug(String.format("Got request for: %s\n%s", request.getRequestURI(), thisTier));

		CallResult callResult = new CallResult();

		for (Method method : thisTier.getMethod()) {
			Integer mode = method.getMode();
			Integer min = method.getMin();
			Integer max = method.getMax();
			String message = method.getMessage();
			MethodA thisMethodA = new ByteBuddy()
  			.subclass(MethodA.class)
				.name("co." + method.getName())
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
				logger.debug(String.format("Result: %s", callResult.getCall()));
			}
		} else{
			logger.debug("No external calls to make...");
		}
		model.addAttribute("results", callResult);
		return "result";
	}

	@RequestMapping(value = "/**", method = RequestMethod.POST, consumes = {
        "application/JSON",
        "application/XML"
    })
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
				.name("co." + method.getName())
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
				logger.debug(String.format("Result: %s", callResult.getCall()));
			}
		} else{
			logger.debug("No external calls to make...");
		}
		return callResult;
	}

}
