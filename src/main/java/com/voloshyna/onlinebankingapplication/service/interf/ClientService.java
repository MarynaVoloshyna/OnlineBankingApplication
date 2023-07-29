package com.voloshyna.onlinebankingapplication.service.interf;

import com.voloshyna.onlinebankingapplication.dto.ClientDTO;
import com.voloshyna.onlinebankingapplication.dto.ManagerForClientDTO;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import com.voloshyna.onlinebankingapplication.entity.Role;
import com.voloshyna.onlinebankingapplication.entity.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ClientService {
    Client createClient(String firstName, String lastName, LocalDate dateOfRegistration, Long taxNumber, String userEmail, String userPassword);
    Client updateClient(Client client);
    Client changeClientManager(String clientEmail, String managerEmail);
    Client changeClientManager(Long clientId, Long managerId);
    List<Client> getAllClientsByManager(String managerEmail);
    Client findClientById(Long clientId);
    List<Client> getAllClientsByManagerId(Long managerId);
    List<Client> getAllClientInDataBase();
    List<Client> getClientsByName(String clientName);
    List<Client> findAllClientsInBank();




}
