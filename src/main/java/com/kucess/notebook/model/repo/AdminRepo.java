package com.kucess.notebook.model.repo;

import com.kucess.notebook.model.entity.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepo extends CrudRepository<Admin, Long> {

	Optional<Admin> findByUserName(String userName);
	
}
