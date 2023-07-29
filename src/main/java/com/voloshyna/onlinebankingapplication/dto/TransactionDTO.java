package com.voloshyna.onlinebankingapplication.dto;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.staticData.TransactionType;

import java.util.Date;

public class TransactionDTO {
    private BankAccount accountFrom;
    private BankAccount accountTo;
    private Double transactionSum;
    private Date transactionDate;
    private TransactionType transactionType;
}
