package com.twitter.sample.rest;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.sample.domain.Tweet;
import com.twitter.sample.exception.DataFormatException;
import com.twitter.sample.exception.ResourceNotFoundException;
import com.twitter.sample.repo.TweetRepository;
import com.twitter.sample.service.TweetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/tweets")
@Api(tags = {"tweets"})
public class TweetRestController extends AbstractRestHandler{

	private TweetRepository repository;
	
	@Autowired
	private TweetService tweetService;

	@Inject
	public void setRepository(TweetRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/json", "application/xml"},
			produces = {"application/json", "application/xml"})
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a tweet resource.", notes = "Returns nothing.")
	public void addTweet(@RequestBody Tweet tweet,
			HttpServletRequest request, HttpServletResponse response) {
		Tweet createdTweet = this.tweetService.createTweet(tweet);
		response.setHeader("Location", request.getRequestURL().append("/").append(createdTweet.getTweetId()).toString());
	}

    @RequestMapping( params = { "page", "size" }, 
    		method = RequestMethod.GET, 
    		produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets all tweets page by page", notes = "Returns a page at a time, provide page no(zero indexed) and size as input ")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                value = "Number of records per page."),
        
    })
    public Page<Tweet> getAllTweetsPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {

        Page<Tweet> resultPage = tweetService.getAllTweets(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
        return resultPage;
    }
   

	 @RequestMapping(value = "/{id}",
	            method = RequestMethod.GET,
	            produces = {"application/json", "application/xml"})
	    @ResponseStatus(HttpStatus.OK)
	    @ApiOperation(value = "Get a single tweet.", notes = "You have to provide a valid tweet ID.")
	    public
	    @ResponseBody
	    Tweet getTweetWithId(@ApiParam(value = "The ID of the tweet.", required = true)
	                             @PathVariable("id") Long id,
	                             HttpServletRequest request, HttpServletResponse response) throws Exception {
	        Tweet tweetFromDb = this.tweetService.find(id);
	        
	        if (tweetFromDb == null) {
				throw new ResourceNotFoundException("resource not found");
			}
	        return tweetFromDb;
	    }
	

	 @RequestMapping(
			 params = {"employee-id"},
			 method = RequestMethod.GET,
			 produces = {"application/json", "application/xml"})
	 @ResponseStatus(HttpStatus.OK)
	 @ApiOperation(value = "Get tweets by employee id", notes = "You have to provide a valid employee ID.")
	 public ResponseEntity<Collection<Tweet>> findTweetWithEmployeeId(@RequestParam(value = "employee-id") int employeeId) {
		 return new ResponseEntity<>(repository.findByEmployeeId(employeeId), HttpStatus.OK);
	 }

	
	@RequestMapping(value = "/{id}",
			method = RequestMethod.PUT,
			consumes = {"application/json", "application/xml"},
			produces = {"application/json", "application/xml"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Update a tweet resource.", notes = "You have to provide a valid tweet ID in the URL and in the payload. The ID attribute can not be updated.")
	public void updateTweetFromDB(@PathVariable("id") long id, @RequestBody Tweet tweet) {
		Tweet tweetFromDb = tweetService.find(id);
		if (tweetFromDb == null) {
			throw new ResourceNotFoundException("resource not found");
		}
		if (id != tweetFromDb.getTweetId()) throw new DataFormatException("ID doesn't match!");
		tweet.setTweetContent(tweet.getTweetContent());
		this.tweetService.updateTweet(tweet);
	}

	@RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE,
			produces = {"application/json", "application/xml"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTweetWithId(@ApiParam(value = "The ID of the existing tweet resource.", required = true)
	@PathVariable("id") Long id, HttpServletRequest request,
	HttpServletResponse response) {
		repository.delete(id);
	}

	@RequestMapping(
			method = RequestMethod.DELETE)
	public void deleteAllTweets() {
		repository.deleteAll();
	}
	
	@RequestMapping(
			value = "/top-tweets/{id}",
			method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Collection<Tweet> getTopTweetsForUser(@PathVariable Integer id) {
		List<Tweet> tweets = tweetService.findTopTweetsForUser(id);
		return tweets;
	}
	
}

