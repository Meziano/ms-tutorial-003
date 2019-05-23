# tutorial-003
## Working with Java Classes
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

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE4NjEyNTc3NzFdfQ==
-->