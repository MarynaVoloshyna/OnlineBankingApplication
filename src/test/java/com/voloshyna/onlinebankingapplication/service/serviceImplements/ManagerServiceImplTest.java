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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ManagerServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Environment environment;
    @InjectMocks
    private ManagerServiceImpl managerService;
    @BeforeEach
    void setup() {
        when(environment.getProperty("friendly.bank.domain")).thenReturn("friendlybank.com");
        managerService.setEnvironment(environment);
    }
    @BeforeEach
    void listSizeSetup(){
        ReflectionTestUtils.setField(managerService, "maxListSize", 3);

    }


    @Test
    void testCreateManager() {
        String firstName = "John";
        String lastName = "Doe";
        String level = "Senior";
        String userEmail = "john.doe@friendlybank.com";
        String userPassword = "password";
        User user = new User();

        when(userRepository.findUserByEmail(any())).thenReturn(user);
        Manager actualManager = managerService.createManager(firstName, lastName, level, userEmail, userPassword);
        assertNotNull(actualManager);
        assertEquals(firstName, actualManager.getFirstName());
        assertEquals(lastName, actualManager.getLastName());
        assertEquals(level, actualManager.getLevel());
        assertEquals(user, actualManager.getUser());
        verify(managerRepository, times(1)).save(actualManager);



    }
    @Test
    void testCreateManagerInvalidUser() {
        String firstName = "John";
        String lastName = "Doe";
        String level = "Senior";
        String userEmail = "john.doe@example.com";
        String userPassword = "password";


        when(userRepository.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                managerService.createManager(firstName, lastName, level, userEmail, userPassword)
        );
        verify(userRepository).findUserByEmail(userEmail);
        verify(managerRepository, never()).save(any(Manager.class));
    }


    @Test
    void testSetClientToManager() {
        String managerEmail = "manager@example.com";
        String clientEmail = "client@example.com";
        Client client = new Client();
        Manager manager = new Manager();
        List<Client> clients = new ArrayList<>();


        manager.setClientList(clients);

        when(clientRepository.findClientByEmail(clientEmail)).thenReturn(client);
        when(managerRepository.findManagerByEmail(managerEmail)).thenReturn(manager);
        when(managerRepository.save(manager)).thenReturn(manager);

        // Act
        managerService.setClientToManager(managerEmail, clientEmail);

        // Assert
        assertEquals(1, clients.size());
        assertEquals(client, clients.get(0));
        assertEquals(manager, client.getManager());
        verify(managerRepository).save(manager);
        verify(clientRepository).save(client);
    }



    @Test
    void testGetAllManager() {
        List<Manager> expectedManagers = new ArrayList<>();
        expectedManagers.add(new Manager());
        expectedManagers.add(new Manager());
        expectedManagers.add(new Manager());

        when(managerRepository.findAll()).thenReturn(expectedManagers);

        // Act
        List<Manager> actualManagers = managerService.getAllManager();

        // Assert
        assertEquals(expectedManagers.size(), actualManagers.size());
        assertEquals(expectedManagers, actualManagers);
        verify(managerRepository).findAll();
    }
}
