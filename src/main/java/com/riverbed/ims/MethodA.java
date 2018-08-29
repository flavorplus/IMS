package com.riverbed.ims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Random;

import com.riverbed.ims.model.MethodResult;

@Component
public class MethodA {

	private final Log logger = LogFactory.getLog(this.getClass());

	private MethodResult result = new MethodResult();

	private Random rng = new Random();

	public MethodResult process(Integer mode, Integer min, Integer max, String message) {
		Integer number = rng.nextInt((max - min) + 1) + min;

		if (message != null) {
			logger.info(message);
		}

		if (mode == 1) {
			cpuSpin(number);
			return this.result;
		} else if (mode == 2) {
			sleepLoad(number, 0.5);
			return this.result;
		} else if (mode == 3) {
			generateData(number);
			return this.result;
		}else {
			this.result.setType("Undifined");
			return this.result;
		}
	}

	private void cpuSpin(Integer number) {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < (number*100); i++){
			double r = rng.nextFloat();
			double v = Math.sin(Math.cos(Math.sin(Math.cos(r))));
		}

		this.result.setType(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.result.setActualRuntime(System.currentTimeMillis() - startTime);
		this.result.setIterations(number);

		logger.debug(String.format("Called %s: The number of itterations was: %d The run time was:%dms", this.result.getType(), this.result.getIterations(), this.result.getActualRuntime()));
		return;
	}

	private void sleepLoad(Integer duration, double load) {
		long startTime = System.currentTimeMillis();
		try {
			// Loop for the given duration
			while (System.currentTimeMillis() - startTime < duration) {
				// Every 100ms, sleep for the percentage of unladen time
				if (System.currentTimeMillis() % 100 == 0) {
					Thread.sleep((long) Math.floor((1 - load) * 100));
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.result.setType(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.result.setActualRuntime(System.currentTimeMillis() - startTime);
		this.result.setDuration(duration);
		this.result.setLoad(load);

		logger.debug(String.format("Called %s: The duration was: %dms with a load off: %.2f The run time was:%dms", this.result.getType(), this.result.getDuration(), this.result.getLoad(), this.result.getActualRuntime()));
		return;
	}

	private void generateData(Integer amount) {
		long startTime = System.currentTimeMillis();
		byte[] buffer = new byte[amount];
		rng.nextBytes(buffer);
		this.result.setPayload(buffer);
		this.result.setLoad(amount);
		this.result.setType(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.result.setActualRuntime(System.currentTimeMillis() - startTime);

		logger.debug(String.format("Called %s: The run time was: %dms The amount of data created was:%d", this.result.getType(), this.result.getActualRuntime(), this.result.getPayload().length));
		return;
	}
}
