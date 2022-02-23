package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import com.kucess.notebook.model.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Map;

@RestController
@RequestMapping("${api.path}")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admins")
    public StatusResponse registerAdmin(@RequestBody AdminIO adminIO){
        adminService.saveAdmin(adminIO);
        return new StatusResponse(Message.ADMIN_REGISTERED, Map.of("username", adminIO.getUserName()));
    }

    @GetMapping("/admins/{userName}")
    public AdminIO getAdmin(@PathVariable String userName){
        return adminService.findAdminByUserName(userName);
    }

    @DeleteMapping("/admins/{userName}")
    public StatusResponse deleteAdmin(@PathVariable String userName){
        adminService.deleteAdminByUserName(userName);
        return new StatusResponse(Message.ADMIN_DELETED, Map.of("username", userName));
    }

    @PutMapping("/admins/{userName}")
    @Transactional
    public StatusResponse updateAdmin(@RequestBody AdminIO adminIO, @PathVariable String userName){
        adminService.updateAdmin(adminIO, userName);
        return new StatusResponse(Message.ADMIN_UPDATED, Map.of("newUsername", adminIO.getUserName()));
    }

}
