package de.meziane.ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import de.meziane.ms.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
}
