package com.voloshyna.onlinebankingapplication.dto;

import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.entity.Currency;

import java.util.List;

public class BankAccountForClientDTO {
    private Currency currency;
    private Double currentSum = 0.0;
    private String accountNumber;
    private List<Transaction> transactionListFrom;
    private List<Transaction> transactionListTo;
}
