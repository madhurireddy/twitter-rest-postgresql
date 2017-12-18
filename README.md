## Plain REST API CRUD with Spring Boot and PostgreSQL.

### Technology stack:

* Spring Boot;
* Spring Web;
* Spring Data;
* PostgreSQL database;
* Hibernate;
* Spring Security (as basic authentication).

#####To run this application use:

mvn spring-boot:run

### The view in the Postman: 
http://localhost:8080/
http://localhost:8080/api/employees
http://localhost:8080/api/tweets
http://localhost:8080/api/v1/tweets/top-tweets/1
http://localhost:8080/api/v1/employees/followers/1
http://localhost:8080/api/v1/employees/1

{
  "employeeId": 4,
  "tweetContent": "hello, I am visiting a museum today",
  "tweetDate": "2017-12-06T17:57:22.803Z",
  
}
