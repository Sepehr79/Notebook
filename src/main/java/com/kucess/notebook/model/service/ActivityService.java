package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.repo.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserService userService;

    private final ActivityRepo activityRepo;

    public void addActivityToEmployee(String adminUserName, String employeeUserName, ActivityIO activityIO){
        Employee employee = userService.getEmployeeByUserName(employeeUserName);
        Admin admin = userService.getAdminByUserName(adminUserName);
        Activity activity = Activity.builder()
                .activityName(activityIO.getActivityName())
                .score(activityIO.getScore())
                .activityDescription(activityIO.getActivityDescription())
                .admin(admin)
                .employee(employee)
                .build();
        employee.addActivity(activity);
        admin.addActivity(activity);
    }

    public void updateActivityFromEmployee(String adminUserName, String employeeUserName, ActivityIO activityIO){
        List<Activity> activities = activityRepo.findByAdminAndEmployee(adminUserName, employeeUserName);
        Activity activity = activities.get(activityIO.getIndex() - 1);
        activity.setActivityName(activityIO.getActivityName());
        activity.setActivityDescription(activityIO.getActivityDescription());
        activity.setScore(activityIO.getScore());
    }

    public void deleteActivityFromEmployee(String adminUserName, String employeeUserName, int index){
        List<Activity> activities = activityRepo.findByAdminAndEmployee(adminUserName, employeeUserName);
        Activity activity = activities.get(index - 1);

        Admin admin = userService.getAdminByUserName(adminUserName);
        Employee employee = userService.getEmployeeByUserName(employeeUserName);

        employee.getActivities().remove(activity);
        admin.getActivities().remove(activity);
        activityRepo.deleteById(activity.getId());
    }

    public List<ActivityIO> findActivityByAdminUserNameAndEmployeeUserName(String adminUserName, String employeeUserName){
        List<Activity> activities = activityRepo.findByAdminAndEmployee(adminUserName, employeeUserName);
        List<ActivityIO> activityIOList = new ArrayList<>();
        int counter = 1;
        for (Activity activity: activities){
            ActivityIO activityIO = new ActivityIO(activity.getActivityName(), activity.getActivityDescription(), activity.getScore());
            activityIO.setAdminUserName(adminUserName);
            activityIO.setIndex(counter++);
            activityIOList.add(activityIO);
        }
        return activityIOList;
    }

}
