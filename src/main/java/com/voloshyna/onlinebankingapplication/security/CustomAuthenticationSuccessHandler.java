package com.voloshyna.onlinebankingapplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("MANAGER"))) {
                setDefaultTargetUrl("/manager/dashboard");
            } else {
                setDefaultTargetUrl("/home/dashboard");
            }
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

