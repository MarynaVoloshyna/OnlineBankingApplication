package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
//@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfRegistration;

    @Column(unique = true, nullable = false)
    private Long taxNumber;
    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "client", fetch = FetchType.EAGER)
    private List<BankAccount> accountList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    private Manager manager;


    public Client(String firstName, String lastName, LocalDate dateOfRegistration, Long taxNumber, User user, Manager manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
        this.taxNumber = taxNumber;
        this.user = user;
        this.manager = manager;
    }


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfRegistration=" + dateOfRegistration +
                ", taxNumber=" + taxNumber +
                '}';
    }

}
