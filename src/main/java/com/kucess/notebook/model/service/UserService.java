package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.entity.Person;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.UserIO;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import com.kucess.notebook.model.repo.PersonRepo;
import com.kucess.notebook.model.service.exception.DuplicateUsernameException;
import com.kucess.notebook.model.service.exception.UserNameNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public abstract class UserService {

    private final AdminRepo adminRepo;
    private final EmployeeRepo employeeRepo;
    private final PersonRepo personRepo;

    @SneakyThrows
    public Admin getAdminByUserName(String userName){
        return adminRepo.findByUserName(userName)
                .orElseThrow((Supplier<Throwable>) () -> new UserNameNotFoundException(userName));
    }

    @SneakyThrows
    public Employee getEmployeeByUserName(String adminUserName){
        return employeeRepo.findByUserName(adminUserName)
                .orElseThrow((Supplier<Throwable>) () -> new UserNameNotFoundException(adminUserName));
    }

    public boolean existsByUsername(String username){
        return personRepo.existsByUserName(username);
    }


}
