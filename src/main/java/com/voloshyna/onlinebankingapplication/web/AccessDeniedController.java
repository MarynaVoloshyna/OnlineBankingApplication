package com.voloshyna.onlinebankingapplication.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    // Redirecting to 403 in case when manager try to access to admin tools
    @GetMapping("/403")
    public String accessDenied(){
        return"access-denied";
    }
}
