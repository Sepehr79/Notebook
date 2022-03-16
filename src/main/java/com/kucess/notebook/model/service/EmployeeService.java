package com.kucess.notebook.model.service;

import com.kucess.notebook.bussiness.IOUserConvertor;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.PersonRepo;
import com.kucess.notebook.model.service.exception.DuplicateUsernameException;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService extends UserService {

    private final IOUserConvertor ioUserConvertor;

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(PersonRepo personRepo, IOUserConvertor ioUserConvertor, PasswordEncoder passwordEncoder, PersonRepo personRepo1) {
        super(personRepo);
        this.ioUserConvertor = ioUserConvertor;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    public void addEmployeeToAdmin(String adminUserName, EmployeeIO employeeIO){
        if (existsByUsername(employeeIO.getUserName()))
            throw new DuplicateUsernameException(employeeIO.getUserName());
        Admin admin = (Admin) getUserByUserName(adminUserName);
        String password = employeeIO.getPassword();
        employeeIO.setPassword(passwordEncoder.encode(password));
        admin.addEmployee(ioUserConvertor.iOToEmployee(employeeIO));
    }

    public void addEmployeeToAdmin(String adminUserName, String employeeUserName){
        Admin admin = (Admin) getUserByUserName(adminUserName);
        Employee employee = (Employee) getUserByUserName(employeeUserName);
        admin.addEmployee(employee);
    }

    public void removeEmployeeFromAdmin(String adminUserName, String employeeUserName){
        Admin admin = (Admin) getUserByUserName(adminUserName);
        Employee employee = (Employee) getUserByUserName(employeeUserName);
        admin.getEmployees().remove(employee);
    }

    public EmployeeIO findEmployeeByUserName(String userName){
        Employee employee = (Employee) getUserByUserName(userName);
        return ioUserConvertor.employeeToIO(employee);
    }

    public void changeEmployeePassword(String userName, String password){
        Employee employee = (Employee) getUserByUserName(userName);
        super.getPersonRepo().save(
                employee.toBuilder().password(passwordEncoder.encode(password)).build()
        );
    }


}
