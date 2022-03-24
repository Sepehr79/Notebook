package com.kucess.notebook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultPageController {

    @GetMapping("/notebook/v1")
    public String redirectToMainPage(){
        return "default";
    }

}
