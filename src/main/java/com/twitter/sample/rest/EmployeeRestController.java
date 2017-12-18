package com.twitter.sample.rest;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.twitter.sample.domain.Employee;
import com.twitter.sample.repo.EmployeeRepository;
import com.twitter.sample.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/employees")
@Api(tags = {"employees"})
public class EmployeeRestController {

	private EmployeeRepository repository;
	
	@Autowired
	private EmployeeService employeeService;

	@Inject
	public void setRepository(EmployeeRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/json", "application/xml"},
			produces = {"application/json", "application/xml"})
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create an employee resource.", notes = "Returns nothing.")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
		return new ResponseEntity<>(repository.save(employee), HttpStatus.CREATED);
	}

	@RequestMapping(
			method = RequestMethod.GET)
	public ResponseEntity<Collection<Employee>> getAllEmployees() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.GET)
	public ResponseEntity<Employee> getEmployeeWithId(@PathVariable Integer id) {
		return new ResponseEntity<>(repository.findOne(id), HttpStatus.OK);
	}

	@RequestMapping(
			params = {"first-name"},
			method = RequestMethod.GET)
	public ResponseEntity<Collection<Employee>> findEmployeeWithFname(@RequestParam(value = "first-name") String firstName) {
		System.out.println("firstName:" +firstName);
		return new ResponseEntity<>(repository.findByFirstName(firstName), HttpStatus.OK);
	}

	@RequestMapping(
			params = {"last-name"},
			method = RequestMethod.GET)
	public ResponseEntity<Collection<Employee>> findEmployeeWithLastName(@RequestParam(value = "last-name") String lastName) {
		return new ResponseEntity<>(repository.findByLastName(lastName), HttpStatus.OK);
	}

	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.PUT)
	public ResponseEntity<Employee> updateEmployeeFromDB(@PathVariable("id") int id, @RequestBody Employee employee) {

		Employee currentEmployee = repository.findOne(id);
		currentEmployee.setFirstName(employee.getFirstName());
		currentEmployee.setLastName(employee.getLastName());
		currentEmployee.setFollowers(employee.getFollowingEmployees());

		return new ResponseEntity<>(repository.save(currentEmployee), HttpStatus.OK);
	}

	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.DELETE)
	public void deleteEmployeeWithId(@PathVariable int id) {
		repository.delete(id);
	}

	@RequestMapping(
			method = RequestMethod.DELETE)
	public void deleteAllEmployees() {
		repository.deleteAll();
	}
	
	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
	
	@RequestMapping(
			value = "/followers/{id}",
			method = RequestMethod.GET)
	public ResponseEntity<Collection<Integer>> getEmployeeFollowersWithId(@PathVariable Integer id) {
		Set<Integer> followers = employeeService.findFollowers(id);

		return new ResponseEntity<>(followers, HttpStatus.OK);
	}


}

