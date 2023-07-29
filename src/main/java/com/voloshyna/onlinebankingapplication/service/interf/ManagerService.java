package com.voloshyna.onlinebankingapplication.service.interf;

import com.voloshyna.onlinebankingapplication.entity.Manager;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManagerService {
    Manager createManager(String firstName, String lastName, String level, String userEmail, String userPassword);
    void setClientToManager(String managerEmail, String clientEmail);
    List<Manager> getAllManager();
    List<Manager> searchManager(String keyword);
    Manager findByEmail(String managerEmail);
    List<Manager> findManagersByEmail(String managerEmail);
    List<Manager> findByClientTaxNumber(Long taxNumber);
    List<Manager> findByClientBankAccountNumber(String bankAccountNumber);
    Manager findById(Long managerId);
    List<Manager> findManagersById(Long managerId);
    List<Manager> findByManagerLevel(String managerLevel);
    List<Manager> findAll();
    Manager saveOrUpdateManager(Long managerId, Manager manager);
    void deleteManager(Long managerId);

}
