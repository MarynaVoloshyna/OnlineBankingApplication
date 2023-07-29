package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "user",fetch = FetchType.EAGER)
    private Client client;
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Manager manager;
   @ManyToOne
   @Enumerated(value = EnumType.STRING)
    private Role role;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
