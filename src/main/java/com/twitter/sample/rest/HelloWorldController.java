package com.twitter.sample.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.sample.service.HelloService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/hello")
@Api(tags = {"hello"})
public class HelloWorldController {
	
	@Autowired
	private HelloService helloService;
	
	@RequestMapping(
			method = RequestMethod.GET)
	public ResponseEntity<String> getEntries() {
		return new ResponseEntity<>("Hello World", HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.GET)
	public ResponseEntity<String> getEntryByKey(@PathVariable String id) {
		String value = this.helloService.getEntry(id);
		if(value != null) {
			return new ResponseEntity<>(value, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Entry was not found.",HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/json", "application/xml"},
			produces = {"application/json", "application/xml"})
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adds a key value .", notes = "Returns String.")
	public ResponseEntity<String> addEntry(@RequestBody Map.Entry<String,String> entry,
			HttpServletRequest request, HttpServletResponse response) {
		this.helloService.addEntry(entry);
		return new ResponseEntity<String>("Added", HttpStatus.CREATED);
	}

	

}
