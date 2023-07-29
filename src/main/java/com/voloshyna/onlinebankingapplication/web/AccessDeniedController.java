package com.voloshyna.onlinebankingapplication.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {
    @GetMapping("/403")
    public String accessDenied(){
        return"access-denied";
    }
}
