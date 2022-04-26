package com.kucess.notebook.controller;

import com.kucess.notebook.bussiness.captcha.CaptchaService;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AdminService adminService;

    @Value("${captcha.site}")
    String captchaSite;

    private final CaptchaService captchaService;

    @GetMapping("/signup")
    public String getAdminsRegisterPage(Model model){
        AdminIO userIO = new AdminIO();
        model.addAttribute("user", userIO);
        model.addAttribute("captchaSite", captchaSite);
        return "signup";
    }

    @PostMapping("/signup")
    public String registerAdmin(@ModelAttribute("user") @Valid AdminIO userIO,
                                BindingResult bindingResult,
                                @RequestParam(name="g-recaptcha-response") String recaptchaResponse,
                                HttpServletRequest request,
                                Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("captchaSite", captchaSite);
            return "signup";
        }
        if (!captchaService.verifyCaptcha(recaptchaResponse, request.getRemoteAddr())){
            return "botDetect";
        }
        adminService.saveAdmin(userIO);
        return "redirect:/login";
    }


}
