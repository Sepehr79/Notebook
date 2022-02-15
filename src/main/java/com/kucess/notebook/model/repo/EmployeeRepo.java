package com.kucess.notebook.model.repo;

import com.kucess.notebook.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmployeeRepo extends CrudRepository<Employee, Long>{

	Optional<Employee> findByUserName(String userName);
	
}
