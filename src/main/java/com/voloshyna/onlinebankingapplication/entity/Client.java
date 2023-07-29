package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(dateOfRegistration, client.dateOfRegistration) && Objects.equals(taxNumber, client.taxNumber) && Objects.equals(user, client.user) && Objects.equals(accountList, client.accountList) && Objects.equals(manager, client.manager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dateOfRegistration, taxNumber, user, accountList, manager);
    }
}
