package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
//@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    private Double currentSum = 0.0;

    private String accountNumber;
    @OneToMany(mappedBy = "accountFrom", cascade = CascadeType.ALL)
    private List<Transaction> transactionListFrom;
    @OneToMany(mappedBy = "accountTo", cascade = CascadeType.ALL)
    private List<Transaction> transactionListTo;
    @Temporal(value = TemporalType.DATE)
    private LocalDate openingDate;
    @ManyToOne
    private Client client;


    public BankAccount(Client client, Currency currency, Double currentSum, String accountNumber, LocalDate openingDate) {
        this.client = client;
        this.currency = currency;
        this.currentSum = currentSum;
        this.accountNumber = accountNumber;
        this.openingDate = openingDate;
    }

    public BankAccount(Client client, Currency currency) {
        this.client = client;
        this.currency = currency;
    }
}
