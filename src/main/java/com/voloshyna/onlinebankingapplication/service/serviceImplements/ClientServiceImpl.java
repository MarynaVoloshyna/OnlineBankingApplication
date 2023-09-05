package com.voloshyna.onlinebankingapplication.service.serviceImplements;


import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import com.voloshyna.onlinebankingapplication.entity.Role;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.repository.ClientRepository;
import com.voloshyna.onlinebankingapplication.repository.ManagerRepository;
import com.voloshyna.onlinebankingapplication.repository.RoleRepository;
import com.voloshyna.onlinebankingapplication.repository.UserRepository;
import com.voloshyna.onlinebankingapplication.service.interf.ClientService;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import jakarta.persistence.EntityNotFoundException;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter

public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @Value("${maxListSize}")
    private Integer maxListSize;

    // Creating new client in system (role depends on user's email)
    @Override
    public Client createClient(String firstName, String lastName, LocalDate dateOfRegistration, Long taxNumber, String userEmail, String userPassword) {
        Manager manager = findFreeManager();
        Role clientRole = roleRepository.findRoleById(3L);
        User user = userService.registrateUser(userEmail, userPassword);
        user.setRole(clientRole);
        userRepository.save(user);
        Client client = new Client(firstName, lastName, LocalDate.now(), taxNumber, user, manager);
        clientRepository.save(client);

        if (manager.getClientList() == null) {
            manager.setClientList(new ArrayList<>());
        }
        managerRepository.save(manager);
        return client;
    }

    // Updating client's data
    @Override
    public Client updateClient(Client client) {

        Client updatingClient = clientRepository.findById(client.getUser().getUserId()).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        clientRepository.save(updatingClient);
        return updatingClient;
    }

    // Set new manager for client using emails
    @Override
    public Client changeClientManager(String clientEmail, String managerEmail) {
        Client client = clientRepository.findClientByEmail(clientEmail);
        Manager manager = managerRepository.findManagerByEmail(managerEmail);
        client.setManager(manager);
        clientRepository.save(client);
        return client;
    }


    // Set new manager for client using ID
    @Override
    public Client changeClientManager(Long clientId, Long managerId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager not found"));
        List<Client> clientsList = manager.getClientList();
        if (clientsList.size() < maxListSize)
            client.setManager(manager);
        clientsList.add(client);
        managerRepository.save(manager);
        clientRepository.save(client);
        return client;
    }

    // Fetch list of clients by manager
    @Override
    public List<Client> getAllClientsByManager(String managerEmail) {
        Manager manager = managerRepository.findManagerByEmail(managerEmail);
        List<Client> clients = clientRepository.findAllByManager(manager);
        return clients;
    }

    // Finding client by his ID in DB
    @Override
    public Client findClientById(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        return client;
    }

    // Fetch client's list by manager ID
    @Override
    public List<Client> getAllClientsByManagerId(Long managerId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager not found"));
        List<Client> clients = clientRepository.findAllByManager(manager);
        return clients;
    }

    // Fetch list of clients registered in service
    @Override
    public List<Client> getAllClientInDataBase() {
        List<Client> clients = clientRepository.findAll();
        return clients;
    }

    // Finding client by his name
    @Override
    public List<Client> getClientsByName(String clientName) {
        return clientRepository.findClientByNameContainsIgnoreCase(clientName);
    }


    //UTIL
    // Finding free manager for client during registration, depends on manager's client list size, which appointed in "application.properties" file

    public Manager findFreeManager() {
        Manager manager = new Manager();
        List<Manager> managers = managerRepository.findAll();
        for (Manager eachManager : managers) {
            if (eachManager.getClientList() == null) {
                eachManager.setClientList(new ArrayList<>());
            }
            if (eachManager.getClientList().size() < maxListSize) {
                manager = eachManager;
                break;
            }
        }
        return manager;
    }


}
