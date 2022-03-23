package com.kucess.notebook.controller;

import com.kucess.notebook.model.entity.*;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.service.ActivityService;
import com.kucess.notebook.model.service.EmployeeService;
import com.kucess.notebook.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("${api.path}")
@RequiredArgsConstructor
public class HomePageController {

    private static final String USER = "user";
    private static final String EMPLOYEES = "employees";
    private static final String EMPLOYEE = "employee";
    private static final String ACTIVITIES = "activities";
    private static final String ACTIVITY = "activity";

    private static final String REDIRECT_TO_HOME_PAGE = "redirect:/notebook/v1/home";

    private final UserService userService;

    private final EmployeeService employeeService;

    private final ActivityService activityService;


    @GetMapping("/home")
    @Transactional
    public String findUser(Authentication authentication, Model model){
        Person person = userService.getUserByUserName(authentication.getName());
        model.addAttribute(USER, person);
        if (person.getAuthorityType() == AuthorityType.ADMIN){
            Set<Employee> employees = ((Admin) person).getEmployees();
            model.addAttribute(EMPLOYEES, employees);
        }else if(person.getAuthorityType() == AuthorityType.EMPLOYEE){
            List<Activity> activities = ((Employee) person).getActivities();
            model.addAttribute(ACTIVITIES, activities);
        }
        return "home";
    }

    @GetMapping("/employees")
    public String addEmployeesPages(Model model){
        EmployeeIO employeeIO = new EmployeeIO();
        model.addAttribute(EMPLOYEE, employeeIO);
        return EMPLOYEE;
    }

    @PostMapping("/employees")
    @Transactional
    public String registerEmployee(@ModelAttribute(EMPLOYEE) @Valid EmployeeIO employeeIO,
                                   BindingResult bindingResult,
                                   Authentication authentication
                                   ){
        if (bindingResult.hasErrors()){
            return EMPLOYEE;
        }
        String adminUserName = authentication.getName();
        employeeService.addEmployeeToAdmin(adminUserName, employeeIO);
        return REDIRECT_TO_HOME_PAGE;
    }

    @PostMapping(value = "/employees/{employeeUserName}/remove")
    @Transactional
    public String removeEmployeeFromAdmin(@PathVariable("employeeUserName") String username, Authentication authentication){
        String adminUserName = authentication.getName();
        employeeService.removeEmployeeFromAdmin(adminUserName, username);
        return REDIRECT_TO_HOME_PAGE;
    }

    @PostMapping("/employees/insert")
    @Transactional
    public String addCurrentEmployee(@RequestParam("userName") String userName, Authentication authentication){
        try {
            employeeService.addEmployeeToAdmin(authentication.getName(), userName);
        }catch (UsernameNotFoundException usernameNotFoundException){
            usernameNotFoundException.printStackTrace();
        }
        return REDIRECT_TO_HOME_PAGE;
    }

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
        return "redirect:/notebook/v1/employees/" + employeeUserName + "/activities";
    }

    @PostMapping("/employees/{empUserName}/activities/{actId}/remove")
    public String deleteActivity(@PathVariable String actId, @PathVariable String empUserName){
        activityService.deleteActivityFromEmployee(Long.parseLong(actId));
        return "redirect:/notebook/v1/employees/" + empUserName + "/activities";
    }

    @GetMapping("/employees/{empUserName}/activities/{actId}")
    public String getUpdatingActivity(@PathVariable String empUserName, @PathVariable String actId, Model model){
        ActivityIO activityById = activityService.findActivityById(Long.parseLong(actId));
        model.addAttribute(ACTIVITY, activityById);
        model.addAttribute("employeeUserName", empUserName);
        return ACTIVITY;
    }

}
