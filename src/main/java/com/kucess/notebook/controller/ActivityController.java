package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import com.kucess.notebook.model.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}")
@Transactional
public class ActivityController {

    private final ActivityService activityService;

    private static final String ADMIN = "admin";
    private static final String EMPLOYEE = "employee";

    @PostMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public StatusResponse addActivityToEmployee(@PathVariable String adminUserName,
                                                @PathVariable String employeeUserName,
                                                @RequestBody ActivityIO activityIO){
        activityService.addActivityToEmployee(adminUserName, employeeUserName, activityIO);
        return new StatusResponse(Message.ACTIVITY_ADDED_TO_EMPLOYEE, Map.of(ADMIN, adminUserName, EMPLOYEE, employeeUserName));
    }

    @GetMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public List<ActivityIO> getAdminEmployeeActivities(@PathVariable String adminUserName,
                                                       @PathVariable String employeeUserName){
        return activityService.findActivityByAdminUserNameAndEmployeeUserName(adminUserName, employeeUserName);
    }

    @DeleteMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities/{index}")
    public StatusResponse removeActivityFromAdminEmployee(@PathVariable String adminUserName,
                                                @PathVariable String employeeUserName,
                                                @PathVariable int index){
        activityService.deleteActivityFromEmployee(adminUserName, employeeUserName, index);
        return new StatusResponse(Message.ACTIVITY_REMOVED_FROM_ADMIN, Map.of(ADMIN, adminUserName, EMPLOYEE, employeeUserName));
    }

    @PutMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public StatusResponse updateActivity(@RequestBody ActivityIO activityIO,
                               @PathVariable String adminUserName,
                               @PathVariable String employeeUserName){
        activityService.updateActivityFromEmployee(adminUserName, employeeUserName, activityIO);
        return new StatusResponse(Message.ACTIVITY_UPDATED, Map.of(ADMIN, adminUserName, EMPLOYEE, employeeUserName));
    }

}
