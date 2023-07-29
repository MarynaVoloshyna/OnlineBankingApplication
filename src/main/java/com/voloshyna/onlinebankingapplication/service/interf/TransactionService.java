package com.voloshyna.onlinebankingapplication.service.interf;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.staticData.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TransactionService {

    Transaction makeTransaction(BankAccount from, BankAccount to, Double amount);
    Transaction primaryTransaction(String accountNumber, Double amount);
    Transaction findTransactionById(Long transactionId);
    List<Transaction> getAllTransactionByBankAccount(BankAccount bankAccount);//client
    List<Transaction> transactionsHistoryByBankAccountNumber(String bankAccountNumber);
    List<Transaction> getAllTransactionByClient(Long clientId);//client
    List<Transaction> getTransactionBySum(Long clientId,Double amount);//client
    List<Transaction> getAllTransactionByTransactionType(Long clientId, TransactionType transactionType);//client

    List<Transaction> getTransactionByPeriod(LocalDateTime startDate, LocalDateTime endDate, Long clientId);
    List<Transaction> findTransactionsByTransactionDateBetweenAndClientId(LocalDateTime startDate, LocalDateTime endDate, Long clientId);
    List<Transaction> findAll();
    List<Transaction> findTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findTransactionsByPeriodAndBankAccountId(LocalDateTime startDate, LocalDateTime endDate, Long bankAccountId);
    List<Transaction> findTransactionsByRecipientBankAccountNumber(String bankAccountNumber);


}
