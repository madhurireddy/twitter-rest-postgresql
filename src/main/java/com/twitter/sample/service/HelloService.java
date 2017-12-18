package com.twitter.sample.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class HelloService {

	private static final Logger log = LoggerFactory.getLogger(TweetService.class);
	private Map<String,String> map = new HashMap<>();
	
	public HelloService() {
		super();
		//this.map = new HashMap<String, String>();
	}
	
	public Map<String, String> getMap() {
		return map;
	}
	
	public void addEntry(Map.Entry<String, String> entry) {
		this.map.put(entry.getKey(), entry.getValue());
	}

	public String getEntry(String key) {
		// TODO Auto-generated method stub
		return this.map.get(key);
	}
	
	
	
}
