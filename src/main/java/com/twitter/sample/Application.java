package com.twitter.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

//		EmployeeRepository employeeRepository = ctx.getBean(EmployeeRepository.class);
//		Employee e = new Employee();
//		e.setFirstName("test");
//		e.setLastName("test");
//		e.setEmail("test@test.com");
//		e.setPhoneNumber("555-555-5555");
//		e.setCreatedDate(new Date());
//		employeeRepository.save(e);
	}
}
