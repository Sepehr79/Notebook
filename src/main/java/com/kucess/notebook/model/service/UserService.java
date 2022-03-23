package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Person;
import com.kucess.notebook.model.repo.PersonRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public abstract class UserService {

    private final PersonRepo personRepo;

    @SneakyThrows
    public Person getUserByUserName(String userName){
        return personRepo.findPersonByUserName(userName)
                .orElseThrow((Supplier<Throwable>) () -> new UsernameNotFoundException(userName));
    }

    public boolean existsByUsername(String username){
        return personRepo.existsByUserName(username);
    }


}
