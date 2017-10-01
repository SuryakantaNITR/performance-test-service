package com.surya.performancetest.service;

import java.util.Arrays;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.surya.performancetest.dto.ResponseDto;

public class RemoteEndpoint implements Callable<Long[]>{
	private static Logger logger = Logger.getLogger(RemoteEndpoint.class);
	
	private String url;
	
	private String email;
	
	private RestTemplate restTemplate;
	
	public RemoteEndpoint(String url, String email) {
		this.url = url;
		this.email = email;
		this.restTemplate = new RestTemplate();
	}
	@Override
	public Long[] call() throws Exception {
		try {
			Long[] longArray = new Long[2];
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Surya-Email-Id", email);
			
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);
			
			logger.info("Hitting remote api for first time. ");
			Long startTime = System.currentTimeMillis();
			ResponseEntity<ResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ResponseDto.class);
			Long endTime = System.currentTimeMillis();
			longArray[0] = endTime-startTime;
			logger.info("Got response from first hit. Total time taken in millis: " + longArray[0]);
			
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				logger.info(responseEntity.getBody());
				
				ResponseDto responseDto = responseEntity.getBody();
				
				HttpHeaders secondHeader = new HttpHeaders();
				secondHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				secondHeader.setContentType(MediaType.APPLICATION_JSON);
				
				MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			    params.add("emailId", responseDto.getEmailId());
			    params.add("uuid", responseDto.getUuid());
				
				HttpEntity<MultiValueMap<String, Object>> requestEntity =
				          new HttpEntity<MultiValueMap<String, Object>>(params, secondHeader);

				logger.info("Hitting remote api for second time. ");
				Long startTimeTwo = System.currentTimeMillis();
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
				Long endTimeTwo = System.currentTimeMillis();
				
				logger.info("Got response from second hit. Total time taken in millis: " + (endTimeTwo - startTimeTwo));
				
				if(response.getStatusCode() == HttpStatus.OK && response.getBody().equals("Success")) {
					longArray[1] = endTimeTwo - startTimeTwo;
				}
				else {
					longArray[1] = null;
					logger.error("Failed response from remote api in second hit");
				}
			}
			else {
				longArray[0] = null;
				logger.error("Failed response from remote api in first hit");
			}
			return longArray;
			
		}
		catch (Exception e) {
			logger.error("Exception while calling remote api " + e);
			return null;
		}
	}

}
