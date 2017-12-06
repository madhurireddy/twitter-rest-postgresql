package com.twitter.sample.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.twitter.sample.domain.Employee;

@RepositoryRestResource
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	List<Employee> findByFirstName(@Param("firstName") String firstName);
	List<Employee> findByLastName(@Param("lastName") String lastName);
}


