package com.kucess.notebook.model.repo;

import org.springframework.data.repository.CrudRepository;

import com.kucess.notebook.model.entity.Activity;

public interface ActivityRepo extends CrudRepository<Activity, Long>{

}
