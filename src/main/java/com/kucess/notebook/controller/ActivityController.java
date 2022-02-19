package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}")
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public void addActivityToEmployee(@PathVariable String adminUserName,
                                      @PathVariable String employeeUserName,
                                      @RequestBody ActivityIO activityIO){
        activityService.addActivityToEmployee(adminUserName, employeeUserName, activityIO);
    }

    @GetMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public List<ActivityIO> getAdminEmployeeActivivties(@PathVariable String adminUserName,
                                                        @PathVariable String employeeUserName){
        return activityService.findActivityByAdminUserNameAndEmployeeUserName(adminUserName, employeeUserName);
    }

    @DeleteMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities/{index}")
    public void removeActivityFromAdminEmployee(@PathVariable String adminUserName,
                                                @PathVariable String employeeUserName,
                                                @PathVariable int index){
        activityService.deleteActivityFromEmployee(adminUserName, employeeUserName, index);
    }

    @PutMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public void updateActivity(@RequestBody ActivityIO activityIO,
                               @PathVariable String adminUserName,
                               @PathVariable String employeeUserName){
        activityService.updateActivityFromEmployee(adminUserName, employeeUserName, activityIO);
    }

}
