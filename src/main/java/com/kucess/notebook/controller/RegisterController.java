package com.kucess.notebook.controller;

import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AdminService adminService;


    @GetMapping("/signup")
    public String getAdminsRegisterPage(Model model){
        AdminIO userIO = new AdminIO();
        model.addAttribute("user", userIO);
        return "signup";
    }

    @PostMapping("/signup")
    public String registerAdmin(@ModelAttribute("user") @Valid AdminIO userIO,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "signup";
        }
        adminService.saveAdmin(userIO);
        return "redirect:/login";
    }


}
