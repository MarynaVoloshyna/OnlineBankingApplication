package com.voloshyna.onlinebankingapplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(@Lazy UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http


                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/", "/home/save","/custom-login", "/register-success","/home/registration", "/static/**", "/home/client-dashboard", "/403", "/manager/registration", "/manager/save", "/login", "/contact")
                .permitAll()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")

                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

                .and()
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/expired")
                .maxSessionsPreventsLogin(false);


        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
