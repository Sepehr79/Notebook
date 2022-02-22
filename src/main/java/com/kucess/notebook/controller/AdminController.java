package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Map;

@RestController
@RequestMapping("${api.path}")
@RequiredArgsConstructor
public class AdminController {

    private static final String STATUS = "status";

    private static final String MESSAGE = "message";

    private final AdminService adminService;

    @PostMapping("/admins")
    public Map<String, String> registerAdmin(@RequestBody AdminIO adminIO){
        adminService.saveAdmin(adminIO);
        return Map.of(STATUS, HttpStatus.OK.name(), MESSAGE, "Admin successfully registered");
    }

    @GetMapping("/admins/{userName}")
    public AdminIO getAdmin(@PathVariable String userName){
        return adminService.findAdminByUserName(userName);
    }

    @DeleteMapping("/admins/{userName}")
    public Map<String, String> deleteAdmin(@PathVariable String userName){
        adminService.deleteAdminByUserName(userName);
        return Map.of(STATUS, HttpStatus.OK.name(), MESSAGE, "Admin successfully deleted", "username", userName);
    }

    @PutMapping("/admins/{userName}")
    @Transactional
    public Map<String, String> updateAdmin(@RequestBody AdminIO adminIO, @PathVariable String userName){
        adminService.updateAdmin(adminIO, userName);
        return Map.of(STATUS, HttpStatus.OK.name(), MESSAGE, "Admin successfully updated", "previousUsername", userName, "newUsername", adminIO.getUserName());
    }

}
