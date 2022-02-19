package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    public EmployeeIO findByUserName(String userName){
        Optional<Employee> employeeOptional = employeeRepo.findByUserName(userName);
        if (employeeOptional.isEmpty())
            throw new IllegalArgumentException();
        Employee employee = employeeOptional.get();
        return EmployeeIO.builder()
                .name(employee.getName())
                .lastName(employee.getLastName())
                .userName(employee.getUserName())
                .activityIOs(activityIOs(employee.getActivities()))
                .build();
    }

    public void changePassword(String userName, String password){
        Optional<Employee> employeeOptional = employeeRepo.findByUserName(userName);
        if (employeeOptional.isEmpty())
            throw new IllegalArgumentException();
        Employee employee = employeeOptional.get();
        employeeRepo.save(
                employee.toBuilder().password(password).build()
        );
    }

    /**
     * @param activities from employee
     */
    private List<ActivityIO> activityIOs(List<Activity> activities){
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
