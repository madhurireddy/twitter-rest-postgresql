package com.twitter.sample.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.twitter.sample.domain.Tweet;

@RepositoryRestResource
public interface TweetRepository extends PagingAndSortingRepository<Tweet, Long> {
	List<Tweet> findByEmployeeId(@Param("employeeId") int employeeId);
	
	List<Tweet> findByEmployeeIds(@Param("employeeIds") Set<Integer> employeeIds);
	//List<Tweet> findByLastName(String name);
	
	Page<Tweet> findAll(Pageable pageable);
}


