package com.riverbed.ims.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Random;

import com.riverbed.ims.model.Result;

@Component
public class MethodA {

	private final Log logger = LogFactory.getLog(this.getClass());

	private Result result = new Result();

	private Random rng = new Random();

	public Result process(Integer mode, Integer min, Integer max) {
		Integer number = rng.nextInt((max - min) + 1) + min;

		if (mode == 1) {
			return cpuSpin(number);
		} else if (mode == 2) {
			return sleepLoad(number, 0.5);
		} else {
			this.result.setType("Undifined");
			return this.result;
		}
	}

	private Result cpuSpin(Integer number) {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < (number*100); i++){
			double r = rng.nextFloat();
			double v = Math.sin(Math.cos(Math.sin(Math.cos(r))));
		}

		this.result.setType(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.result.setActualRuntime(System.currentTimeMillis() - startTime);
		this.result.setIterations(number);

		logger.debug(String.format("Called %s: The number of itterations was: %d The run time was:%dms", this.result.getType(), this.result.getIterations(), this.result.getActualRuntime()));
		return this.result;
	}

	private Result sleepLoad(Integer duration, double load) {
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
		return this.result;
	}
}
