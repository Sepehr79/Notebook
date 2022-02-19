package com.kucess.notebook.bussiness;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IOUserConvertor {


    public AdminIO adminToIO(Admin admin){
        return AdminIO.builder()
                .userName(admin.getUserName())
                .name(admin.getName())
                .lastName(admin.getLastName())
                .employeeIOs(employeeToIO(admin.getEmployees()))
                .build();
    }

    public Employee iOToEmployee(EmployeeIO employeeIO){
        return Employee.builder()
                .name(employeeIO.getName())
                .lastName(employeeIO.getLastName())
                .userName(employeeIO.getUserName())
                .password(employeeIO.getPassword())
                .authorityType(AuthorityType.EMPLOYEE)
                .build();
    }

    public List<EmployeeIO> employeeToIO(List<Employee> employees){
        if (employees != null && !employees.isEmpty()){
            return employees.stream().map(employee -> EmployeeIO.builder()
                    .name(employee.getName())
                    .lastName(employee.getLastName())
                    .userName(employee.getUserName())
                    .activityIOs(activityToIO(employee.getActivities()))
                    .build()).collect(Collectors.toList());
        }
        return List.of();
    }


    public List<AdminIO> adminToIO(List<Admin> admins){
        if (admins != null && !admins.isEmpty()){
            return admins.stream().map(admin -> AdminIO.builder().name(admin.getName()).lastName(admin.getLastName()).build()).collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * @param activities from employee
     */
    public List<ActivityIO> activityToIO(List<Activity> activities){
        if (activities != null && !activities.isEmpty()){
            return activities.stream().map(activity ->
                    {
                        ActivityIO activityIO = new ActivityIO(activity.getActivityName(), activity.getActivityDescription(), activity.getScore());
                        activityIO.setAdminUserName(activity.getAdmin().getUserName());
                        return activityIO;
                    }
            ).collect(Collectors.toList());
        }
        return List.of();
    }

}
