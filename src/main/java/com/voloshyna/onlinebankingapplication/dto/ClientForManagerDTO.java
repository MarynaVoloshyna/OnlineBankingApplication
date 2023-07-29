package com.voloshyna.onlinebankingapplication.dto;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientForManagerDTO {
    private String firstName;
    private String lastName;
    private Date dateOfRegistration;
    private List<BankAccountForManagerDTO> accountList = new ArrayList<>();
}
