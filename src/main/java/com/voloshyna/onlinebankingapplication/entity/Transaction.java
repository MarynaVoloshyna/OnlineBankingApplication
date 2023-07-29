package com.voloshyna.onlinebankingapplication.entity;

import com.voloshyna.onlinebankingapplication.staticData.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "accountFrom")
    private BankAccount accountFrom;
    @ManyToOne
    @JoinColumn(name = "accountTo")
    private BankAccount accountTo;
    private Double transactionSum;
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;




    public Transaction(BankAccount accountFrom, BankAccount accountTo, Double transactionSum, LocalDateTime transactionDate) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transactionSum = transactionSum;
        this.transactionDate = transactionDate;
    }

    public Transaction(BankAccount accountFrom, Double transactionSum, LocalDateTime transactionDate, TransactionType transactionType) {
        this.accountFrom = accountFrom;
        this.transactionSum = transactionSum;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }


    public Transaction(BankAccount accountFrom, Double transactionSum, LocalDateTime transactionDate, TransactionType transactionType, BankAccount accountTo) {
        this.accountFrom = accountFrom;
        this.transactionSum = transactionSum;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.accountTo = accountTo;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                ", transactionSum=" + transactionSum +
                ", transactionDate=" + transactionDate +
                ", transactionType=" + transactionType +
                '}';
    }
}
