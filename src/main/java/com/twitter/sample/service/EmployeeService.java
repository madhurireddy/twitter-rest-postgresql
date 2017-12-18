package com.twitter.sample.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twitter.sample.domain.Employee;
import com.twitter.sample.repo.EmployeeRepository;

/*
 * Sample service to demonstrate what the API would use to get things done
 */
@Service
public class EmployeeService {

	private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	public EmployeeService() {
	}

	public Set<Integer> findFollowers(int employeeId){
//		Queue<Integer> toVisit = new ArrayDeque<>();
//		toVisit.add(employeeId);
//
//		Map<Integer, Integer> visited = new LinkedHashMap<>();
//
//		while (toVisit.size()>0) {
//			Integer node = toVisit.poll();
//
//			visited.put(node, 1);
//			//NOTE : we come out if we know the first 10 people i.e. followers
//			if (visited.size() == 10) {
//				break;
//			}
			Employee e = employeeRepository.getOne(employeeId);
			List<Employee> neighbors = e.getFollowingEmployees();
			Set<Integer> toReturn = new HashSet<>();
			toReturn.add(employeeId);
			for (int i = 0; i < neighbors.size(); i++) {
				//if (!visited.containsKey(neighbors.get(i).getEmployeeId())) {
				//	toVisit.add(neighbors.get(i).getEmployeeId());
				//}
				
				toReturn.add(neighbors.get(i).getEmployeeId());
			}

//		}
		return toReturn;
	}

	public Employee createE(Employee Employee) {
		return employeeRepository.save(Employee);
	}

	public Employee getEmployee(int id) {
		return employeeRepository.findOne(id);
	}

	public void updateEmployee(Employee Employee) {
		employeeRepository.save(Employee);
	}

	public void deleteEmployee(int id) {
		employeeRepository.delete(id);
	}

}
