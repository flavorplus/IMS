
package com.riverbed.ims.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Random;

@Component
public class MethodA {

	private final Log logger = LogFactory.getLog(this.getClass());

	private Random rng = new Random();

	public String process(Integer mode, Integer min, Integer max) {
		Integer number = rng.nextInt((max - min) + 1) + min;

		if (mode == 1) {
			return cpuSpin(number);
		} else if (mode == 2) {
			return sleepLoad(number, 0.5);
		} else {
			return "Nothing....";
		}
	}

	private String cpuSpin(Integer number) {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < (number*100); i++){
			double r = rng.nextFloat();
			double v = Math.sin(Math.cos(Math.sin(Math.cos(r))));
		}

		long runTime = System.currentTimeMillis() - startTime;

		logger.debug(String.format("Called cpuSpin: The number of itterations was: %d The run time was:%dms", number, runTime));

		return "----";
	}

	private String sleepLoad(Integer duration, double load) {
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

		long runTime = System.currentTimeMillis() - startTime;

		logger.debug(String.format("Called sleepLoad: The duration was: %dms The run time was:%dms", duration, runTime));

		return "+++++";
	}
}
