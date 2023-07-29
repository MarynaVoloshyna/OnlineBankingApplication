package com.voloshyna.onlinebankingapplication.repository;

import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findManagerByLevelIgnoreCase(String level);
    List<Manager> findManagerByLevelContainsIgnoreCase(String level);
    @Query("SELECT m FROM Manager as m WHERE m.user.email=:email")
    Manager findManagerByEmail(@Param("email") String email);
    @Query("SELECT m FROM Manager as m WHERE m.user.email LIKE %:email%")
    List<Manager> findManagersByEmail(@Param("email") String email);
    @Query ("SELECT m FROM Manager m JOIN m.clientList c JOIN c.accountList a WHERE a.accountNumber LIKE %:clientAccountNumber%")
    List<Manager> findManagerByClientAccountNumber(@Param("clientAccountNumber") String clientAccountNumber);
    @Query("SELECT m FROM Manager m WHERE m.lastName LIKE %:keyword% OR  m.firstName LIKE %:keyword%")
    List <Manager> findManagerByName(@Param("keyword") String keyword);
    @Query("SELECT m FROM Manager m JOIN m.clientList c WHERE CAST (c.taxNumber AS string) LIKE %:clientTaxNumber%")
    List <Manager> findManagerByClientTaxNumber(@Param("clientTaxNumber") Long clientTaxNumber);
    List<Manager> findManagersByIdIsContaining(Long managerId);





}
