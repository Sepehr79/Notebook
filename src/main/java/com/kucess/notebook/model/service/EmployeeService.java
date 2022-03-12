package com.kucess.notebook.model.service;

import com.kucess.notebook.bussiness.IOUserConvertor;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import com.kucess.notebook.model.repo.PersonRepo;
import com.kucess.notebook.model.service.exception.DuplicateUsernameException;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService extends UserService {

    private final IOUserConvertor ioUserConvertor;

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepo employeeRepo, AdminRepo adminRepo, PersonRepo personRepo, IOUserConvertor ioUserConvertor, PasswordEncoder passwordEncoder, PersonRepo personRepo1) {
        super(adminRepo, employeeRepo, personRepo);
        this.ioUserConvertor = ioUserConvertor;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    public void addEmployeeToAdmin(String adminUserName, EmployeeIO employeeIO){
        if (existsByUsername(employeeIO.getUserName()))
            throw new DuplicateUsernameException(employeeIO.getUserName());
        Admin admin = getAdminByUserName(adminUserName);
        String password = employeeIO.getPassword();
        employeeIO.setPassword(passwordEncoder.encode(password));
        admin.addEmployee(ioUserConvertor.iOToEmployee(employeeIO));
    }

    public void addEmployeeToAdmin(String adminUserName, String employeeUserName){
        Admin admin = getAdminByUserName(adminUserName);
        Employee employee = getEmployeeByUserName(employeeUserName);
        admin.addEmployee(employee);
    }

    public void removeEmployeeFromAdmin(String adminUserName, String employeeUserName){
        Admin admin = getAdminByUserName(adminUserName);
        Employee employee = getEmployeeByUserName(employeeUserName);
        admin.getEmployees().remove(employee);
    }

    public EmployeeIO findEmployeeByUserName(String userName){
        Employee employee = getEmployeeByUserName(userName);
        return ioUserConvertor.employeeToIO(employee);
    }

    public void changeEmployeePassword(String userName, String password){
        Employee employee = getEmployeeByUserName(userName);
        super.getEmployeeRepo().save(
                employee.toBuilder().password(passwordEncoder.encode(password)).build()
        );
    }


}
