package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.repository.BankAccountRepository;
import com.voloshyna.onlinebankingapplication.repository.TransactionRepository;
import com.voloshyna.onlinebankingapplication.staticData.CurrencyRate;
import com.voloshyna.onlinebankingapplication.staticData.TransactionType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
   @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionServiceImpl transactionService;
    @BeforeEach
    void setup(){
        ReflectionTestUtils.setField(transactionService, "maxForeignCurrency", 100.0);
    }

    @Test
    void testPrimaryTransactionWithZeroCurrentSum() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.USD);
        bankAccount.setCurrentSum(0.0);
        String accountNumber = "123456789";
        Double amount = 100.0;
        Transaction expectedTransaction = new Transaction();


        when(bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase(accountNumber))
                .thenReturn(bankAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
      Transaction transaction =   transactionService.primaryTransaction(accountNumber, amount);
      assertEquals(amount, transaction.getTransactionSum());

    }

    @Test
    void testPrimaryTransactionWithNoZeroCurrentSum(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.USD);
        bankAccount.setCurrentSum(1.0);
        String accountNumber = "123456789";
        Double amount = 100.0;
        when(bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase(accountNumber)).thenReturn(bankAccount);
        assertThrows(IllegalArgumentException.class, ()->{
            transactionService.primaryTransaction(accountNumber, amount);
        });
    }

    @Test
    void makeTransaction() {
    }
    @Test
    public void testMakeTransactionSufficientBalance() {
        // Arrange
        BankAccount senderAccount = new BankAccount();
        senderAccount.setCurrentSum(1000.0);
        senderAccount.setCurrency(Currency.USD);

        BankAccount recipientAccount = new BankAccount();
        recipientAccount.setCurrentSum(500.0);
        recipientAccount.setCurrency(Currency.USD);

        Double amount = 200.0;

        when(bankAccountRepository.save(senderAccount)).thenReturn(senderAccount);
        when(bankAccountRepository.save(recipientAccount)).thenReturn(recipientAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        Transaction result = transactionService.makeTransaction(senderAccount, recipientAccount, amount);

        // Assert
       assertEquals(TransactionType.WITHDRAW, result.getTransactionType());
       assertEquals(amount, result.getTransactionSum());

        assertEquals(800.0, senderAccount.getCurrentSum());
        assertEquals(700.0, recipientAccount.getCurrentSum());

        verify(bankAccountRepository, times(1)).save(senderAccount);
        verify(bankAccountRepository, times(1)).save(recipientAccount);
        verify(transactionRepository, times(1)).save(result);
    }
    @Test
    public void testMakeTransactionInsufficientBalance() {
        // Arrange
        BankAccount senderAccount = new BankAccount();
        senderAccount.setCurrentSum(100.0);

        BankAccount recipientAccount = new BankAccount();
        recipientAccount.setCurrentSum(500.0);

        Double amount = 200.0;

        // Act and Assert
       assertThrows(IllegalArgumentException.class, () -> {
            transactionService.makeTransaction(senderAccount, recipientAccount, amount);
        });
    }
    @Test
    public void testMakeTransactionRecipientAccountNotFound() {
        // Arrange
        BankAccount senderAccount = new BankAccount();
        senderAccount.setCurrentSum(1000.0);

        BankAccount recipientAccount = null;

        Double amount = 200.0;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            transactionService.makeTransaction(senderAccount, recipientAccount, amount);
        });
    }



    @Test
    void testGetAllTransactionByBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123456789");

        List<Transaction> expectedTransactions = new ArrayList<>();
        // Add some transactions to the expectedTransactions list

        when(transactionRepository.findAllByAccountFrom_AccountNumberContains(bankAccount.getAccountNumber()))
                .thenReturn(expectedTransactions);

        // Act
        List<Transaction> actualTransactions = transactionService.getAllTransactionByBankAccount(bankAccount);

        // Assert
       assertEquals(expectedTransactions, actualTransactions);

        verify(transactionRepository, times(1))
                .findAllByAccountFrom_AccountNumberContains(bankAccount.getAccountNumber());
    }


    @Test
    void getAllTransactionByClient() {
        Long clientId = 123L;

        List<Transaction> expectedTransactions = new ArrayList<>();
        // Add some transactions to the expectedTransactions list

        when(transactionRepository.findTransactionsByClient(clientId))
                .thenReturn(expectedTransactions);

        // Act
        List<Transaction> actualTransactions = transactionService.getAllTransactionByClient(clientId);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);

        verify(transactionRepository, times(1))
                .findTransactionsByClient(clientId);
    }



    @Test
    void getTransactionBySum() {
    }

    @Test
    void testGetAllTransactionByTransactionType() {
        Long clientId = 123L;
        TransactionType transactionType = TransactionType.DEPOSIT;

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionType(transactionType);
        Transaction transaction2 = new Transaction();
        transaction2.setTransactionType(transactionType);
        Transaction transaction3 = new Transaction();
        transaction3.setTransactionType(transactionType);
        Transaction transaction4 = new Transaction();
        transaction4.setTransactionType(transactionType);

        List<Transaction> allTransactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4);
        when(transactionRepository.findTransactionsByClient(clientId)).thenReturn(allTransactions);

        // Act
        List<Transaction> transactionsByType = transactionService.getAllTransactionByClient(clientId);

        // Assert

        verify(transactionRepository).findTransactionsByClient(clientId);

        assertEquals(4, transactionsByType.size());
        for (Transaction transaction : transactionsByType) {
            assertEquals(transactionType, transaction.getTransactionType());
        }
    }

    @Test
    void getTransactionByPeriod() {
        Long clientId = 15L;
        LocalDateTime startDate = LocalDateTime.now(); // Set the start date
        LocalDateTime endDate = LocalDateTime.now(); // Set the end date

        List<Transaction> expectedTransactions = new ArrayList<>();
        // Add some transactions to the expectedTransactions list

        when(transactionRepository.findTransactionsByTransactionDateBetween(startDate, endDate))
                .thenReturn(expectedTransactions);

        // Act
        List<Transaction> actualTransactions = transactionService.getTransactionByPeriod(LocalDateTime.MIN, LocalDateTime.MAX, clientId);

        // Assert
       assertEquals(expectedTransactions, actualTransactions);

        verify(transactionRepository, times(1))
                .findTransactionsByTransactionDateBetween(startDate, endDate);

    }

    @Test
    void calculateTransactionSumEURToUSD() {
        Double amount = 500.0;
        BankAccount senderAccount = new BankAccount();
        senderAccount.setCurrency(Currency.EUR);
        BankAccount recipientAccount = new BankAccount();
        recipientAccount.setCurrency(Currency.USD);

        // Act
        Double expectedTransactionAmount = amount / CurrencyRate.getCrossRate();
        Double transactionAmount = transactionService.calculateTransactionSum(senderAccount, recipientAccount, amount);

        // Assert
        assertEquals(expectedTransactionAmount, transactionAmount);
    }
    @Test
    void testCalculateTransactionSumUSDToUAH() {
        // Arrange
        Double amount = 500.0;
        BankAccount senderAccount = new BankAccount();
        senderAccount.setCurrency(Currency.USD);
        BankAccount recipientAccount = new BankAccount();
        recipientAccount.setCurrency(Currency.UAH);

        // Act
        Double expectedTransactionAmount = amount * CurrencyRate.getUSD();
        Double transactionAmount = transactionService.calculateTransactionSum(senderAccount, recipientAccount, amount);

        // Assert
        assertEquals(expectedTransactionAmount, transactionAmount);
    }
}