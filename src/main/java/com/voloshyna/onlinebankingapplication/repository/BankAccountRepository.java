package com.voloshyna.onlinebankingapplication.repository;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

  BankAccount findBankAccountByCurrency(Currency currency);
  BankAccount findBankAccountByAccountNumberContainsIgnoreCase(String keyNumber);
  List<BankAccount> findBankAccountsByAccountNumberContainsIgnoreCase(String keyNumber);
  List <BankAccount> findBankAccountByClient_TaxNumber(Long taxNumber);
  List <BankAccount> findByOpeningDate(LocalDate localDate);

  Integer countByClient_IdAndCurrency(Long userId, Currency currency);
  @Query("SELECT b FROM BankAccount b JOIN b.client WHERE b.client.user.userId=:clientId")
  List <BankAccount> findAllByClientId(@Param("clientId") Long clientId);





}
