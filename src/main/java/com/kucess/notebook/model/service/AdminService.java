package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo adminRepo;

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
        Optional<Admin> adminOptional = adminRepo.findByUserName(userName);
        if (adminOptional.isEmpty()){
            throw new IllegalArgumentException();
        }
        Admin admin = adminOptional.get();
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
        Optional<Admin> adminOptional = adminRepo.findByUserName(userName);
        if (adminOptional.isEmpty()){
            throw new IllegalArgumentException();
        }
        adminRepo.delete(adminOptional.get());
    }

    /**
     * Find admin by username
     * @param userName unique identifier
     * @return admin
     */
    public AdminIO findByUserName(String userName){
        Optional<Admin> adminOptional = adminRepo.findByUserName(userName);
        if (adminOptional.isPresent()){
            Admin admin = adminOptional.get();
            return adminIO(admin);
        }
        throw new IllegalArgumentException();
    }

    private AdminIO adminIO(Admin admin){
        return AdminIO.builder()
                .userName(admin.getUserName())
                .name(admin.getName())
                .lastName(admin.getLastName())
                .employeeIOs(employeeIOs(admin.getEmployees()))
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
