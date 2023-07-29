package com.voloshyna.onlinebankingapplication.dto;

import com.voloshyna.onlinebankingapplication.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerDTO {
    private String firstName;
    private String lastName;
    private String level;
    private List<Client> clientList;

}
