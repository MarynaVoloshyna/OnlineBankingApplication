package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import com.voloshyna.onlinebankingapplication.repository.BankAccountRepository;
import com.voloshyna.onlinebankingapplication.repository.ClientRepository;
import com.voloshyna.onlinebankingapplication.staticData.CurrencyRate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    void createBankAccount() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(bankAccountService.checkingBankAccount(client.getId(), Currency.USD)).thenReturn(0);
        BankAccount bankAccount = bankAccountService.createBankAccount(1L, Currency.USD);
        verify(bankAccountRepository).save(any());
        assertEquals(client, bankAccount.getClient());
        assertEquals(Currency.USD, bankAccount.getCurrency());
    }

    @Test
    void getBankAccountCurrentAmount() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        BankAccount bankAccount = new BankAccount(client, Currency.USD, 1000.0, "1234567890123456", new Date());
        client.setAccountList(Collections.singletonList(bankAccount));
        Double currentAmount = bankAccountService.getBankAccountCurrentAmount(1L,"1234567890123456" );
        assertEquals(1000.0, currentAmount, 10.0);



    }

    @Test
    void getBankAccountsTotalSum() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        BankAccount bankAccountUSD = new BankAccount(client, Currency.USD, 100.0, "1234567890123456", new Date());
        BankAccount bankAccountEUR = new BankAccount(client, Currency.EUR, 100.0, "2345678901234567", new Date());
        BankAccount bankAccountUAH = new BankAccount(client, Currency.UAH, 100.0, "3456789012345678", new Date());
        client.setAccountList(Arrays.asList(bankAccountUSD, bankAccountEUR, bankAccountUAH));
        Double totalSum = bankAccountService.getBankAccountsTotalSum(1L);
        Double expectedSum = bankAccountUSD.getCurrentSum()* CurrencyRate.getUSD() + bankAccountEUR.getCurrentSum()*CurrencyRate.getEUR()+bankAccountUAH.getCurrentSum();
        assertEquals(expectedSum, totalSum, 0.0);



    }


    @Test
    void findBankAccountById() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccount1 = bankAccountService.findBankAccountById(1L);
        assertEquals(bankAccount,bankAccount1 );
    }

    @Test
    void findBankAccountByAccountNumber() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("4444444444444444");
        when(bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase(any())).thenReturn(bankAccount);
        BankAccount bankAccount1 = bankAccountService.findBankAccountByAccountNumber("4444444444444444");
        assertEquals(bankAccount,bankAccount1 );
    }
    @Test
    void testExceptionForNotFoundBankAccountById(){
        assertThrows(EntityNotFoundException.class, ()->bankAccountService.findBankAccountById(50L));
    }
}