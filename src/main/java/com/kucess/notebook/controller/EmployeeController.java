package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}")
public class EmployeeController {

    private EmployeeService employeeService;

    @PostMapping("/employees/{userName}")
    public EmployeeIO findEmployeeByUserName(@PathVariable String userName){
        return employeeService.findByUserName(userName);
    }

    @PostMapping("/employees")
    public void changePassword(@RequestParam(name = "user") final String userName,
                               @RequestParam(name = "pass") final String password){
        employeeService.changePassword(userName, password);
    }

}
