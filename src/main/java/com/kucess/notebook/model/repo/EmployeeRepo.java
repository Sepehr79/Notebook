package com.kucess.notebook.model.repo;

import org.springframework.data.repository.CrudRepository;

import com.kucess.notebook.model.entity.Employee;

public interface EmployeeRepo extends CrudRepository<Employee, Long>{

	
	
}
