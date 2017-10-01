package com.surya.performancetest.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
	
	private static Logger logger = Logger.getLogger(MetricsService.class);
	
	@Autowired
	private Environment env;
	
	private Map<String, Double> statistics;
	private List<Long> responseTime;
	private ExecutorService threadPool;
	
	public Map<String, Double> getPerformanceMetrics() {
		logger.info("Inside Service: " + MetricsService.class.getName());
		responseTime = new ArrayList<>();
		statistics = new HashMap<>();
		collectResponse();
		Collections.sort(responseTime);
		int totalSize = responseTime.size();
		logger.info("Total size of response list: " + totalSize);
		
		if(totalSize > 100) {
			statistics.put("10th percentile", responseTime.get(getIndex(10, totalSize)).doubleValue());
			statistics.put("50th percentile", responseTime.get(getIndex(50, totalSize)).doubleValue());
			statistics.put("90th percentile", responseTime.get(getIndex(90, totalSize)).doubleValue());
			statistics.put("95th percentile", responseTime.get(getIndex(95, totalSize)).doubleValue());
			statistics.put("99th percentile", responseTime.get(getIndex(99, totalSize)).doubleValue());
			statistics.put("Mean", calculateMean());
			statistics.put("Standard Deviation", standardDeviation());
		}
		else {
			statistics.put("error", -1D);
		}
		
		return statistics;
	}
	
	private void collectResponse() {
		this.threadPool = Executors.newFixedThreadPool(20);
		
		List<Future<Long[]>> list = new ArrayList<>();
		Callable<Long[]> callable = new RemoteEndpoint(env.getProperty("remote.api.url"), env.getProperty("remote.api.email"));
		int count = 0;
		while(count<100) {
			Future<Long[]> future = threadPool.submit(callable);
			list.add(future);
			count++;
		}
		threadPool.shutdown();
		try {
		  threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			logger.info("Exception while awaiting..");
		}
		logger.info("Completion of all requests to the server");
		
		for(Future<Long[]> future: list) {
			try {
				Long[] element = future.get();
				responseTime.add(element[0]);
				responseTime.add(element[1]);
			}
			catch(Exception ex) {
				logger.error("Exception while unpacking future." + ex);
			}
		}
	}

	private int getIndex(int nthPercentile, int totalSize) {
		if(totalSize >= 100) {
			int factor = totalSize/100;
			return nthPercentile*factor - 1;
		}
		return -1;
	}
	
	private Double calculateMean() {
		Long sum = 0L;
		for(Long time: responseTime) {
			sum += time;
		}
		return (double)(sum/responseTime.size());
	}
	
	private Double standardDeviation() {
		Double sum = 0D;
        Double mean = calculateMean();
 
        for (Long time : responseTime)
            sum += Math.pow((time - mean), 2);
        
        return Math.sqrt( sum /responseTime.size());
	}
}
