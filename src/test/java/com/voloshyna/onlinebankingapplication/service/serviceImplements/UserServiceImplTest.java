package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.Role;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.repository.RoleRepository;
import com.voloshyna.onlinebankingapplication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registrateUser() {
        String email = "test@example.com";
        String password = "password123";
        User user = new User(email, password);
        when(userRepository.save(user)).thenReturn(user);
        User expectedUser = userService.registrateUser(email, password);
        assertEquals(user.getEmail(), expectedUser.getEmail());
        assertEquals(user.getPassword(), expectedUser.getPassword());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void setRoleToUserWithCorporateDomain() {

        String email = "example@friendlybank.com";
        User user = new User(email, "password", null);
        Role managerRole = new Role("MANAGER");
        Role clientRole = new Role("USER");
        when(roleRepository.findRoleByNameIgnoreCase("Manager")).thenReturn(managerRole);
        when(roleRepository.findRoleByNameIgnoreCase("User")).thenReturn(clientRole);
        when(userRepository.findUserByEmail(email)).thenReturn(user);

        User result = userService.setRoleToUser(email);
        assertEquals(managerRole, result.getRole());
        assertEquals(user, result);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(userRepository, times(1)).save(result); // Ensure the save operation is called

    }

    @Test
    void setRoleToUserWithExternalDomain(){
        String email = "example@external.com";
        User user = new User(email, "password", null);
        Role managerRole = new Role("MANAGER");
        Role clientRole = new Role("USER");
        when(roleRepository.findRoleByNameIgnoreCase("Manager")).thenReturn(managerRole);
        when(roleRepository.findRoleByNameIgnoreCase("User")).thenReturn(clientRole);
        when(userRepository.findUserByEmail(email)).thenReturn(user);
        User result = userService.setRoleToUser(email);
        assertEquals(clientRole, result.getRole());
        assertEquals(user, result);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(userRepository, times(1)).save(user);



    }

    @Test
    void getUsersByRoleManager() {
        Role managerRole = new Role("MANAGER");
        Role userRole = new Role("USER");
        List<User> users = new ArrayList<>();
        users.add(new User("example@friendlybank.com", "password", managerRole));
        users.add(new User("example@external.com", "password", userRole));
        users.add(new User("example@friendlybank.com", "password", managerRole));
        users.add(new User("example1@external.com", "password", userRole));
        users.add(new User("example1@friendlybank.com", "password1", managerRole));
        when(userRepository.findUserByRole(managerRole)).thenReturn(users);
        List<User> expectedUsers = userService.getUsersByRole((managerRole));
        assertEquals(users, expectedUsers);
        verify(userRepository, times(1)).findUserByRole(managerRole);
    }

    @Test
    void getUserByRoleName() {
        String roleName = "MANAGER";
        Role managerRole = roleRepository.findRoleByNameIgnoreCase(roleName);
        List<User> users = new ArrayList<>();
        users.add(new User("example@friendlybank.com", "password", managerRole));
        users.add(new User("example@friendlybank.com", "password", managerRole));
        users.add(new User("example1@friendlybank.com", "password1", managerRole));
        when(userRepository.findUserByRoleName(roleName)).thenReturn(users);
        List<User> expectedUsers = userService.getUserByRoleName(roleName);
        assertEquals(users, expectedUsers);
        verify(userRepository, times(1)).findUserByRoleName(roleName);



    }
}