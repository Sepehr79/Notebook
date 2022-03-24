package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("${api.path}")
@RequiredArgsConstructor
public class ActivitiesPageController {

    private static final String EMPLOYEE_ACTIVITIES = "redirect:/notebook/v1/employees/%s/activities";
    private static final String ACTIVITIES = "activities";
    private static final String ACTIVITY = "activity";

    private final ActivityService activityService;

    @GetMapping("/employees/{employeeUserName}/activities")
    public String getActivities(
            @PathVariable String employeeUserName,
            Authentication authentication,
            Model model
    ){
        String adminUserName = authentication.getName();
        List<ActivityIO> activityIOList =
                activityService.findActivityByAdminUserNameAndEmployeeUserName(adminUserName, employeeUserName);
        model.addAttribute(ACTIVITIES, activityIOList);
        model.addAttribute("empUserName", employeeUserName);
        return "employeeActivities";
    }

    @GetMapping("/employees/{employeeUserName}/activities/insert")
    public String addActivity(@PathVariable String employeeUserName, Model model){
        ActivityIO activityIO = new ActivityIO();
        model.addAttribute(ACTIVITY, activityIO);
        model.addAttribute("employeeUserName", employeeUserName);
        return ACTIVITY;
    }

    @PostMapping("/employees/{employeeUserName}/activities/insert")
    @Transactional
    public String addActivity(
            @ModelAttribute(ACTIVITY) @Valid ActivityIO activityIO,
            BindingResult bindingResult,
            Authentication authentication,
            @PathVariable String employeeUserName
    ){
        if (bindingResult.hasErrors()){
            return ACTIVITY;
        }
        String adminUserName = authentication.getName();
        if (activityIO.getId() == 0)
            activityService.addActivityToEmployee(adminUserName, employeeUserName, activityIO);
        else
            activityService.updateActivity(activityIO);
        return String.format(EMPLOYEE_ACTIVITIES, employeeUserName);
    }

    @PostMapping("/employees/{empUserName}/activities/{actId}/remove")
    public String deleteActivity(@PathVariable String actId, @PathVariable String empUserName){
        activityService.deleteActivity(Long.parseLong(actId));
        return String.format(EMPLOYEE_ACTIVITIES, empUserName);
    }

    @GetMapping("/employees/{empUserName}/activities/{actId}")
    public String getUpdatingActivity(@PathVariable String empUserName, @PathVariable String actId, Model model){
        ActivityIO activityById = activityService.findActivityById(Long.parseLong(actId));
        model.addAttribute(ACTIVITY, activityById);
        model.addAttribute("employeeUserName", empUserName);
        return ACTIVITY;
    }

}
