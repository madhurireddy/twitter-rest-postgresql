package com.twitter.sample.service;

import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.twitter.sample.domain.Tweet;
import com.twitter.sample.repo.TweetRepository;

@Service
public class TweetService {

    private static final Logger log = LoggerFactory.getLogger(TweetService.class);

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    EmployeeService employeeService;

    public TweetService() {
    }

	public Page<Tweet> findTopTweetsForUser(Integer employeeId, int page, int size) {
		Set<Integer> employeeIds = employeeService.findFollowers(employeeId);
		
		log.info("followers:" + employeeIds);
		if(employeeIds == null || employeeIds.isEmpty()) {
			return new PageImpl<Tweet>(new ArrayList<Tweet>());
		}
		return tweetRepository.findByEmployeeIds(employeeIds, new PageRequest(page, size));
	}
	
	public Tweet find(long tweetId) {
        return tweetRepository.findOne(tweetId);
    }
	
	public Iterable<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    public Tweet createTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    public void updateTweet(Tweet tweet) {
        tweetRepository.save(tweet);
    }

    public void deleteTweet(Long id) {
        tweetRepository.delete(id);
    }
    
    public Page<Tweet> getAllTweets(int page, int size) {
        return tweetRepository.findAll(new PageRequest(page, size));
    }
    
}
