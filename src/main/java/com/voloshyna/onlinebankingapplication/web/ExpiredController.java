package com.voloshyna.onlinebankingapplication.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expired")
public class ExpiredController {
    @GetMapping
    public String sessionIsExpiredMessage(){
        return "expired-message";
    }
}
