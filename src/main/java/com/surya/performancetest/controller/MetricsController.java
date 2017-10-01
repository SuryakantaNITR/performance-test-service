package com.surya.performancetest.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.surya.performancetest.service.MetricsService;

@RestController
@RequestMapping(value="/metrics")
public class MetricsController {

	private static Logger logger = Logger.getLogger(MetricsController.class);
	
	@Autowired
	private MetricsService metricsService;
	
	@GetMapping(value="/get")
	public @ResponseBody Map<String, Double> performanceTest() {
		logger.info("Inside controller: " + MetricsController.class.getName());
		return metricsService.getPerformanceMetrics(); 
	}
}
