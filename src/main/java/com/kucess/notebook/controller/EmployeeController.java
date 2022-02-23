package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import com.kucess.notebook.model.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/admins/{userName}/employees")
    @Transactional
    public StatusResponse addEmployeeToAdmin(@RequestBody EmployeeIO employeeIO, @PathVariable String userName){
        employeeService.addEmployeeToAdmin(userName ,employeeIO);
        return new StatusResponse(Message.EMPLOYEE_ADD_TOO_ADMIN, Map.of("adminUsername", userName, "employeeUsername", employeeIO.getUserName()));
    }

    @PutMapping("/admins/{adminUserName}/employees/{employeeUserName}")
    public StatusResponse addCurrentEmployeeToAdmin(@PathVariable String adminUserName, @PathVariable String employeeUserName){
        employeeService.addEmployeeToAdmin(adminUserName, employeeUserName);
        return new StatusResponse(Message.CURRENT_EMPLOYEE_ADD_TO_ADMIN,Map.of("adminUsername", adminUserName, "employeeUsername", employeeUserName));
    }

    @DeleteMapping("/admins/{adminUserName}/employees/{employeeUserName}")
    public StatusResponse removeEmployeeFromAdmin(@PathVariable String adminUserName, @PathVariable String employeeUserName){
        employeeService.removeEmployeeFromAdmin(adminUserName, employeeUserName);
        return new StatusResponse(Message.EMPLOYEE_REMOVED_FROM_ADMIN, Map.of("adminUsername", adminUserName, "employeeUsername", employeeUserName));
    }

    @GetMapping("/employees/{userName}")
    public EmployeeIO findEmployeeByUserName(@PathVariable String userName){
        return employeeService.findEmployeeByUserName(userName);
    }

    @PostMapping("/employees")
    public StatusResponse changePassword(@RequestParam(name = "user") final String userName,
                               @RequestParam(name = "pass") final String password){
        employeeService.changeEmployeePassword(userName, password);
        return new StatusResponse(Message.EMPLOYEE_PASSWORD_CHANGE, Map.of("employeeUsername", userName));
    }

}
