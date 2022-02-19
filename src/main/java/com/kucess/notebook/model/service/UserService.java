package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class UserService {

    private final AdminRepo adminRepo;
    private final EmployeeRepo employeeRepo;

    public Admin getAdminByUserName(String userName){
        return adminRepo.findByUserName(userName)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Employee getEmployeeByUserName(String adminUserName){
        return employeeRepo.findByUserName(adminUserName)
                .orElseThrow(IllegalArgumentException::new);
    }

}
