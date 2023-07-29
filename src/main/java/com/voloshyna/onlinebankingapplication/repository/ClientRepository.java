package com.voloshyna.onlinebankingapplication.repository;

import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import com.voloshyna.onlinebankingapplication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
        Client findClientByManager(Manager manager);
        @Query("SELECT c FROM Client c JOIN c.accountList a WHERE a.accountNumber LIKE %:keyNumber%")
        Client findClientByAccountNumberContains(@Param("keyNumber") String keyNumber);

//        @Query("SELECT c FROM Client as c WHERE c.firstName like %:name% or c.lastName LIKE %:name% ")
//        List <Client> findClientByNameContainsIgnoreCase(@Param("name") String name);
        @Query("SELECT c FROM Client c WHERE c.firstName LIKE %:name% OR c.lastName LIKE %:name%")
        List<Client> findClientByNameContainsIgnoreCase(@Param("name") String name);

        @Query("SELECT c FROM Client as c WHERE c.user.email=:email")
        Client findClientByEmail(@Param("email") String email);
        List<Client> findAllByManager(Manager manager);
        @Query("SELECT c FROM Client as c WHERE c.user.role=:role")
        List<Client> findAllByRole(@Param("role") Role role);

        List<Client> findAll();


}
