package com.voloshyna.onlinebankingapplication.dto;

import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.entity.Currency;

import java.util.Date;
import java.util.List;

public class BankAccountForManagerDTO {
    private Currency currency;
    private Double currentSum = 0.0;
    private String accountNumber;
    private List<Transaction> transactionListFrom;
    private List<Transaction> transactionListTo;
    private Date openingDate;
    private ClientForManagerDTO client;
}
