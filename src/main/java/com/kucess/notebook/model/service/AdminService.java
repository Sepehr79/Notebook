package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo adminRepo;

    private final EmployeeRepo employeeRepo;

    /**
     * Register new admin
     */
    public void saveAdmin(AdminIO adminIO){
        Admin admin = Admin.builder()
                .name(adminIO.getName())
                .lastName(adminIO.getLastName())
                .userName(adminIO.getUserName())
                .password(adminIO.getPassword())
                .authorityType(AuthorityType.ADMIN)
                .build();
        adminRepo.save(admin);
    }

    /**
     * Update admin
     */
    public void updateAdmin(AdminIO adminIO, String userName){
        Admin admin = findAdminOrThrowException(userName);
        adminRepo.save(
                admin.toBuilder()
                    .name(adminIO.getName())
                    .lastName(adminIO.getLastName())
                    .userName(adminIO.getUserName())
                    .password(adminIO.getPassword())
                .build()
        );

    }

    /**
     * Delete admin
     */
    public void deleteByUserName(String userName){
        adminRepo.delete(findAdminOrThrowException(userName));
    }

    /**
     * Find admin by username
     * @param userName unique identifier
     * @return admin
     */
    public AdminIO findByUserName(String userName){
        Admin admin = findAdminOrThrowException(userName);
        return adminIO(admin);
    }

    public void addEmployeeToAdmin(String adminUserName, EmployeeIO employeeIO){
        Admin admin = findAdminOrThrowException(adminUserName);
        admin.addEmployee(employee(employeeIO));
    }

    public void addEmployeeToAdmin(String adminUserName, String employeeUserName){
        Admin admin = findAdminOrThrowException(adminUserName);
        Employee employee = findEmployeeOrThrowException(employeeUserName);
        admin.addEmployee(employee);
    }

    public EmployeeIO removeEmployeeFromAdmin(String adminUserName, String employeeUserName){
        Admin admin = findAdminOrThrowException(adminUserName);
        Employee employee = findEmployeeOrThrowException(employeeUserName);
        admin.getEmployees().remove(employee);
        return employeeIO(employee);
    }

    private Admin findAdminOrThrowException(String userName){
        return adminRepo.findByUserName(userName)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Employee findEmployeeOrThrowException(String adminUserName){
        return employeeRepo.findByUserName(adminUserName)
                .orElseThrow(IllegalArgumentException::new);
    }

    private EmployeeIO employeeIO(Employee employee){
        return EmployeeIO.builder()
                .name(employee.getName())
                .lastName(employee.getLastName())
                .userName(employee.getUserName())
                .build();
    }


    private AdminIO adminIO(Admin admin){
        return AdminIO.builder()
                .userName(admin.getUserName())
                .name(admin.getName())
                .lastName(admin.getLastName())
                .employeeIOs(employeeIOs(admin.getEmployees()))
                .build();
    }

    private Employee employee(EmployeeIO employeeIO){
        return Employee.builder()
                .name(employeeIO.getName())
                .lastName(employeeIO.getLastName())
                .userName(employeeIO.getUserName())
                .password(employeeIO.getPassword())
                .authorityType(AuthorityType.EMPLOYEE)
                .build();
    }

    private List<EmployeeIO> employeeIOs(List<Employee> employees){
        if (employees != null && !employees.isEmpty()){
            return employees.stream().map(employee -> EmployeeIO.builder()
                    .name(employee.getName())
                    .lastName(employee.getLastName())
                    .userName(employee.getUserName())
                    .activityIOs(activityIOs(employee.getActivities()))
                    .build()).collect(Collectors.toList());
        }
        return List.of();
    }

    private List<ActivityIO> activityIOs(List<Activity> activities){
        if (activities != null && !activities.isEmpty()){
            return activities.stream().map(activity -> new ActivityIO(activity.getActivityName(), activity.getActivityDescription(), activity.getScore())).collect(Collectors.toList());
        }
        return List.of();
    }


}
