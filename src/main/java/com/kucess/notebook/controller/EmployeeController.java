package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/admins/{userName}/employees")
    @Transactional
    public void addEmployeeToAdmin(@RequestBody EmployeeIO employeeIO, @PathVariable String userName){
        employeeService.addEmployeeToAdmin(userName ,employeeIO);
    }

    @PutMapping("/admins/{adminUserName}/employees/{employeeUserName}")
    public void addCurrentEmployeeToAdmin(@PathVariable String adminUserName, @PathVariable String employeeUserName){
        employeeService.addEmployeeToAdmin(adminUserName, employeeUserName);
    }

    @DeleteMapping("/admins/{adminUserName}/employees/{employeeUserName}")
    public void removeEmployeeFromAdmin(@PathVariable String adminUserName, @PathVariable String employeeUserName){
        employeeService.removeEmployeeFromAdmin(adminUserName, employeeUserName);
    }

    @GetMapping("/employees/{userName}")
    public EmployeeIO findEmployeeByUserName(@PathVariable String userName){
        return employeeService.findEmployeeByUserName(userName);
    }

    @PostMapping("/employees")
    public void changePassword(@RequestParam(name = "user") final String userName,
                               @RequestParam(name = "pass") final String password){
        employeeService.changeEmployeePassword(userName, password);
    }

}
