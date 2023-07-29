package com.voloshyna.onlinebankingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerForClientDTO {
    private String firstName;
    private String lastName;
}
