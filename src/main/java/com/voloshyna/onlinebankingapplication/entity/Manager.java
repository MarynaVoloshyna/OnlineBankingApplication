package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String level;
    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Client> clientList;


    public Manager(String firstName, String lastName, String level, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.user = user;
    }


    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
