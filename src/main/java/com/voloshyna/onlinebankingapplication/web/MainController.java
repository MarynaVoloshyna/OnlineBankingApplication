package com.voloshyna.onlinebankingapplication.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    // Show welcome page
    @GetMapping
    public String showMainPage(){
        return "main-view/friendlybank";
    }
    @GetMapping ("/contact")
    public String showContact(){return "contact";}
}
