package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("${api.path}")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admins")
    public void registerAdmin(@RequestBody AdminIO adminIO){
        adminService.saveAdmin(adminIO);
    }

    @GetMapping("/admins/{userName}")
    public AdminIO getAdmin(@PathVariable String userName){
        return adminService.findByUserName(userName);
    }

    @DeleteMapping("/admins/{userName}")
    public void deleteAdmin(@PathVariable String userName){
        adminService.deleteByUserName(userName);
    }

    @PutMapping("/admins/{userName}")
    public void updateAdmin(@RequestBody AdminIO adminIO, @PathVariable String userName){
        adminService.updateAdmin(adminIO, userName);
    }

    @PostMapping("/admins/{userName}/employees")
    @Transactional
    public void addEmployeeToAdmin(@RequestBody EmployeeIO employeeIO, @PathVariable String userName){
        adminService.addEmployeeToAdmin(userName ,employeeIO);
    }

    @PutMapping("/admins/{adminUserName}/employees/{employeeUserName}")
    public void addCurrentEmployeeToAdmin(@PathVariable String adminUserName, @PathVariable String employeeUserName){
        adminService.addEmployeeToAdmin(adminUserName, employeeUserName);
    }

    @DeleteMapping("/admins/{adminUserName}/employees/{employeeUserName}")
    public void removeEmployeeFromAdmin(@PathVariable String adminUserName, @PathVariable String employeeUserName){
        adminService.removeEmployeeFromAdmin(adminUserName, employeeUserName);
    }

    @PostMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public void addActivityToEmployee(@PathVariable String adminUserName,
                                      @PathVariable String employeeUserName,
                                      @RequestBody ActivityIO activityIO){
        adminService.addActivityToEmployee(adminUserName, employeeUserName, activityIO);
    }

    @GetMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public List<ActivityIO> getAdminEmployeeActivivties(@PathVariable String adminUserName,
                                                        @PathVariable String employeeUserName){
        return adminService.findByAdminUserNameAndEmployeeUserName(adminUserName, employeeUserName);
    }

    @DeleteMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities/{index}")
    public void removeActivityFromAdminEmployee(@PathVariable String adminUserName,
                                                @PathVariable String employeeUserName,
                                                @PathVariable int index){
        adminService.deleteActivityFromEmployee(adminUserName, employeeUserName, index);
    }

    @PutMapping("/admins/{adminUserName}/employees/{employeeUserName}/activities")
    public void updateActivity(@RequestBody ActivityIO activityIO,
                               @PathVariable String adminUserName,
                               @PathVariable String employeeUserName){
        adminService.updateActivityFromEmployee(adminUserName, employeeUserName, activityIO);
    }

}
