package com.kucess.notebook.model.service;

import com.kucess.notebook.bussiness.IOUserConvertor;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService extends UserService {

    private final IOUserConvertor ioUserConvertor;

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepo employeeRepo, AdminRepo adminRepo, IOUserConvertor ioUserConvertor, PasswordEncoder passwordEncoder) {
        super(adminRepo, employeeRepo);
        this.ioUserConvertor = ioUserConvertor;
        this.passwordEncoder = passwordEncoder;
    }

    public void addEmployeeToAdmin(String adminUserName, EmployeeIO employeeIO){
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
