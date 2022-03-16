package com.kucess.notebook.model.repo;

import com.kucess.notebook.model.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepo extends CrudRepository<Person, Long> {

    Optional<Person> findPersonByUserName(String username);

    boolean existsByUserName(String userName);

}
