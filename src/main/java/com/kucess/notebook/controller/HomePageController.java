package com.kucess.notebook.controller;

import com.kucess.notebook.model.entity.*;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.service.AdminService;
import com.kucess.notebook.model.service.EmployeeService;
import com.kucess.notebook.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("${api.path}")
@RequiredArgsConstructor
public class HomePageController {

    private final UserService userService;

    private final EmployeeService employeeService;

    private final AdminService adminService;

    @GetMapping("/welcome")
    public String findUser(Authentication authentication, Model model){
        Person person = userService.getUserByUserName(authentication.getName());
        model.addAttribute("user", person);
        if (person.getAuthorityType() == AuthorityType.ADMIN){
            List<Employee> employees = ((Admin) person).getEmployees();
            model.addAttribute("employees", employees);
        }else if(person.getAuthorityType() == AuthorityType.EMPLOYEE){
            List<Activity> activities = ((Employee) person).getActivities();
            model.addAttribute("activities", activities);
        }
        return "welcome";
    }

    @GetMapping("/employees")
    public String addEmployeesPages(Model model){
        EmployeeIO employeeIO = new EmployeeIO();
        model.addAttribute("employee", employeeIO);
        return "addEmployee";
    }

    @PostMapping("/employees")
    @Transactional
    public String registerEmployee(@ModelAttribute("employee") @Valid EmployeeIO employeeIO, Authentication authentication){
        String adminUserName = authentication.getName();
        employeeService.addEmployeeToAdmin(adminUserName, employeeIO);
        return "redirect:/notebook/v1/welcome";
    }

    @PostMapping(value = "/employees/{employeeUserName}")
    @Transactional
    public String removeEmployeeFromAdmin(@PathVariable("employeeUserName") String username, Authentication authentication){
        String adminUserName = authentication.getName();
        employeeService.removeEmployeeFromAdmin(adminUserName, username);
        return "redirect:/notebook/v1/welcome";
    }

}
