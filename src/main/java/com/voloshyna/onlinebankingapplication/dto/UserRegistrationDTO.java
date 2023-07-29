package com.voloshyna.onlinebankingapplication.dto;


import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserRegistrationDTO {
    private String email;
    private String password;
}
