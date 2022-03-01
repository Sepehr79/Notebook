package com.kucess.notebook.security;

import com.kucess.notebook.model.repo.PersonRepo;
import com.kucess.notebook.model.service.exception.UserNameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepo personRepo;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PersonDetails(personRepo.findPersonByUserName(username).orElseThrow((Supplier<Throwable>) () -> new UserNameNotFoundException(username)));
    }
}
