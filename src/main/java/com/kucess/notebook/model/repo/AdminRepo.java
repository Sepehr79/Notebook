package com.kucess.notebook.model.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kucess.notebook.model.entity.Admin;

public interface AdminRepo extends CrudRepository<Admin, Long> {

	Optional<Admin> findByUserName(String userName);
	
}
