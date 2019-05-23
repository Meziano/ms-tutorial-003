# Microservices Tutorial-003
## ## Make our rest-services comminucate using Java Classes
Now let's assume we want to retrieve the *employees* for a given a departmentId as Java objects.
### The domain classes
In this case we must add the *Employee* Java class to the domain package of **department-service** and modify both the *Department* Java Class to reflect  the fact that a *department* has a list of *employees*:
```
package de.meziane.ms.domain;
...
@Entity
public class Department {

  @javax.persistence.Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long Id;
  private String name;
  private String description;
  private List<Employee> employees;
  ....
  @OneToMany
  @JoinColumn(name="department_id", referencedColumnName = "id")
  public List<Employee> getEmployees() {
    return employees;
  }
  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }
}

package de.meziane.ms.domain;
...
@Entity
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long Id;
  @Column(name="DEPARTMENT_ID")
  private Long departmentId;
  ...
}
```
### The data for the database population at startup
We still use the same *data.sql* file to populate the database at startup. 
It must be clear that **department-service** and the **employee-service** are using 2 independant databases.
### The DepartmentController
We need to change the end-point introduced  to retrieve a *department* with its *employees* as we want rather to work with Java classes. Here is a first pseudo-code:
```
...
@GetMapping("/departments/with-employees/{id}")
public ObjectNode findByIdWithEmployees(@PathVariable Long id) {
  // retrieve the department
  Department dept = departmentRepository.getOne(id);
  // send a GET to the employees-service to get the employees having departmentId= id 
  // add the employees to  dept 
  return dept /* with its employees*/;
}
...
```
We will use the [exchange](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html#exchange-java.lang.String-org.springframework.http.HttpMethod-org.springframework.http.HttpEntity-org.springframework.core.ParameterizedTypeReference-java.util.Map-) method of the [**RestTemplate**](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html) to communicate with the **employees-service** and get the *employees* for a given *department*:   
```
...
@GetMapping("/departments/with-employees/{id}")
public ObjectNode findByIdWithEmployees(@PathVariable Long id) {
  Department dept = departmentRepository.getOne(id);
  RestTemplate restTemplate = new RestTemplate();
  String EmployeeResourceUrl = "http://localhost:8082/{id}/employees";
  ResponseEntity<List<Employee>> response = restTemplate.exchange(EmployeeResourceUrl, HttpMethod.GET, null,
   new ParameterizedTypeReference<List<Employee>>() {}, id);
  List<Employee> employees = response.getBody();	
  dept.setEmployees(employees);
  return dept;
}
```
We start both services as Spring Boot Applications and we request http://localhome:8081/departments/with-employees/1 and we get the *"IT" department* with the list of all its *employees*.

!["IT"-Department with its Employees](images/[findEmployeesByDepartmentIdUsingJavaClasses.png](https://github.com/Meziano/tutorial-003/blob/master/images/findEmployeesByDepartmentIdUsingJavaClasses.png "findEmployeesByDepartmentIdUsingJavaClasses.png")?raw=true)

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE1MDExMTU0MTIsLTE4NjEyNTc3NzFdfQ
==
-->