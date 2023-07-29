package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.repository.ClientRepository;
import com.voloshyna.onlinebankingapplication.repository.ManagerRepository;
import com.voloshyna.onlinebankingapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientServiceImpl clientService;
    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(clientService, "maxListSize", 3);
    }

    @Test
    void testCreateClient() {
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dateOfRegistration = LocalDate.now();
        Long taxNumber = 1234567890L;
        String userEmail = "john.doe@example.com";
        String userPassword = "111";

        Manager manager = new Manager();
        List<Client> clientList = new ArrayList<>();
        manager.setClientList(clientList);

        User user = new User(userEmail, "password");

        // Create the necessary objects
        Client client = new Client( firstName, lastName, dateOfRegistration, taxNumber, user, manager);
        client.setFirstName(firstName);
        client.setLastName(lastName);



        // Mock the necessary methods
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(userRepository.findUserByEmail(anyString())).thenReturn(mock(User.class));


        Client createdClient = clientService.createClient( firstName, lastName, dateOfRegistration, taxNumber, userEmail, userPassword);
        assertEquals(client.getTaxNumber(), createdClient.getTaxNumber());
        verify(clientRepository).save(any(Client.class));
    }



    @Test
    void testChangeClientManager() {
        String clientEmail = "client@example.com";
        String managerEmail = "manager@example.com";
        Client client = new Client();
        Manager manager = new Manager();

        when(clientRepository.findClientByEmail(clientEmail)).thenReturn(client);
        when(managerRepository.findManagerByEmail(managerEmail)).thenReturn(manager);
        Client updatedClient = clientService.changeClientManager(clientEmail, managerEmail);
        assertEquals(manager, updatedClient.getManager());
        verify(clientRepository).save(updatedClient);


    }

    @Test
    void testGetAllClientsByManager() {
        String managerEmail = "manager@example.com";
        Manager manager = new Manager();
        List<Client> expectedClients = new ArrayList<>();
        expectedClients.add(new Client());
        expectedClients.add(new Client());

        when(managerRepository.findManagerByEmail(managerEmail)).thenReturn(manager);
        when(clientRepository.findAllByManager(manager)).thenReturn(expectedClients);

        List<Client> actualClients = clientService.getAllClientsByManager(managerEmail);

        assertEquals(expectedClients, actualClients);

    }

    @Test
    void testFindFreeManager() {

        Manager manager1 = new Manager();
        manager1.setId(1L);
        manager1.setFirstName("Manager 1");
        List<Client> clients1 = new ArrayList<>(1);
        clients1.add(new Client());
        manager1.setClientList(clients1);

        Manager manager2 = new Manager();
        manager2.setId(2L);
        manager2.setFirstName("Manager 2");
        List<Client> clients2 = new ArrayList<>(2);
        clients2.add(new Client());
        clients2.add(new Client());
        manager2.setClientList(clients2);

        Manager manager3 = new Manager();
        manager3.setId(3L);
        manager3.setFirstName("Manager 3");
        List<Client> clients3 = new ArrayList<>(3);
        clients3.add(new Client());
        clients3.add(new Client());
        clients3.add(new Client());
        manager3.setClientList(clients3);

        List<Manager> managers = Arrays.asList(manager1, manager2, manager3);
        when(managerRepository.findAll()).thenReturn(managers);
        Manager freeManager = clientService.findFreeManager();
        assertEquals(manager1, freeManager);




    }
}