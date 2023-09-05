package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();

}
