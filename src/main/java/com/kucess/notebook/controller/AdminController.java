package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

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
        return adminService.findAdminByUserName(userName);
    }

    @DeleteMapping("/admins/{userName}")
    public void deleteAdmin(@PathVariable String userName){
        adminService.deleteAdminByUserName(userName);
    }

    @PutMapping("/admins/{userName}")
    @Transactional
    public void updateAdmin(@RequestBody AdminIO adminIO, @PathVariable String userName){
        adminService.updateAdmin(adminIO, userName);
    }

}
