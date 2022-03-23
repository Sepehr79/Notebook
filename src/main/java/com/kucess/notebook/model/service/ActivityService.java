package com.kucess.notebook.model.service;

import com.kucess.notebook.bussiness.convertor.IOUserConvertor;
import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.repo.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserService userService;
    private final ActivityRepo activityRepo;
    private final IOUserConvertor ioUserConvertor;

    public void updateActivity(ActivityIO activityIO){
        Optional<Activity> byId = activityRepo.findById(activityIO.getId());
        if (byId.isEmpty())
            throw new IllegalArgumentException();
        byId.get().setActivityName(activityIO.getActivityName());
        byId.get().setActivityDescription(activityIO.getActivityDescription());
        byId.get().setScore(activityIO.getScore());
    }

    public void addActivityToEmployee(String adminUserName, String employeeUserName, ActivityIO activityIO){
        Admin admin = (Admin) userService.getUserByUserName(adminUserName);
        Employee employee = (Employee) userService.getUserByUserName(employeeUserName);
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

    public void updateActivityFromEmployee(ActivityIO activityIO){
        Optional<Activity> activityOptional = activityRepo.findById(activityIO.getId());
        if (activityOptional.isEmpty())
            throw new IllegalArgumentException("Activity not found");
        Activity activity = activityOptional.get();
        activity.setActivityName(activityIO.getActivityName());
        activity.setActivityDescription(activityIO.getActivityDescription());
        activity.setScore(activityIO.getScore());
    }

    public void deleteActivity(long id){
        Optional<Activity> activity = activityRepo.findById(id);
        if (activity.isEmpty())
            throw new IllegalArgumentException("Activity not found with the given id");
        activity.get().getAdmin().getActivities().remove(activity.get());
        activity.get().getEmployee().getActivities().remove(activity.get());
        activityRepo.delete(activity.get());
    }

    public List<ActivityIO> findActivityByAdminUserNameAndEmployeeUserName(String adminUserName, String employeeUserName){
        List<Activity> activities = activityRepo.findByAdminAndEmployee(adminUserName, employeeUserName);
        List<ActivityIO> activityIOList = new ArrayList<>();
        for (Activity activity: activities){
            ActivityIO activityIO = ioUserConvertor.activityToIO(activity);
            activityIO.setId(activity.getId());
            activityIOList.add(activityIO);
        }
        return activityIOList;
    }

    public ActivityIO findActivityById(long id){
        Optional<Activity> activity = activityRepo.findById(id);
        if (activity.isEmpty())
            throw new IllegalArgumentException("Activity not found");
        return ioUserConvertor.activityToIO(activity.get());
    }

}
