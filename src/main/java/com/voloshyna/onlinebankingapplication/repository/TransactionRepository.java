package com.voloshyna.onlinebankingapplication.repository;

import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.staticData.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionByAccountToContains(String accountNumber);
    Transaction findTransactionByAccountTo_AccountNumber(String accountNumber);
    Transaction findTransactionByTransactionDate(Date transactionDate);
    List<Transaction> findTransactionByTransactionSum(Double transactionSum);
    List<Transaction> findAllByAccountFrom_AccountNumberContains(String accountNumber);
    List<Transaction> findTransactionsByTransactionType(TransactionType transactionType);
    List<Transaction> findTransactionsByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

@Query("SELECT t FROM Transaction t WHERE (t.transactionDate BETWEEN :startDate AND :endDate) AND (t.accountFrom.client.id = :clientId OR t.accountTo.client.id = :clientId)")
List<Transaction> findTransactionsByTransactionDateBetweenAndClientId(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("clientId") Long clientId
);
@Query("SELECT t FROM Transaction t WHERE (t.transactionDate BETWEEN :startDate AND :endDate) AND (t.accountFrom.id=:bankAccountId )")
List<Transaction> findTransactionsByTransactionDateBetweenAndAccountId(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("bankAccountId") Long bankAccountId
);


    @Query("SELECT t FROM Transaction t JOIN t.accountFrom a JOIN a.client c WHERE c.id = :clientId")
    List<Transaction> findTransactionsByClient(@Param("clientId") Long clientId);




}
