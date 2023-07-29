package com.voloshyna.onlinebankingapplication.service.interf;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Currency;

import java.util.Date;
import java.util.List;

public interface BankAccountService {
    BankAccount createBankAccount(Long clientId,Currency currency);
    BankAccount findBankAccountById(Long bankAccountId);
    BankAccount findBankAccountByAccountNumber(String accountNumber);
    Double getBankAccountCurrentAmount(Long clientId, String accountNumber);
    Double getBankAccountsTotalSum(Long clientId);
    List<BankAccount> getBankAccountsByClient(Long clientId);
    List<BankAccount> findBankAccountsByAccountNumber(String accountNumber);

}
