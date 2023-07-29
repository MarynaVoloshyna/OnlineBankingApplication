package com.voloshyna.onlinebankingapplication.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component

public class ClientDTO {
    private String firstName;
    private String lastName; //TODO: подумать, нужна ли фамилия клиенту?
    private List<BankAccountForClientDTO> accountList = new ArrayList<>();
    private ManagerDTO manager;
}
