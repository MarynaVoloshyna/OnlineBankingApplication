package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.entity.Role;

import com.voloshyna.onlinebankingapplication.repository.RoleRepository;
import com.voloshyna.onlinebankingapplication.repository.UserRepository;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(@Lazy UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    //Registration of new user in system
    @Override
    public User registrateUser(String email, String password) {
        User userChecking = userRepository.findUserByEmail(email);
        if (userChecking != null) throw new RuntimeException("User with email" + email + "already exist");
        String safePassword = encoder.encode(password);
        User user = new User(email, safePassword);
        userRepository.save(user);
        return user;
    }

    // Set role to user, depending on his email domain
    @Override
    public User setRoleToUser(String userEmail) {
        Role managerRole = roleRepository.findRoleByNameIgnoreCase("Manager");
        Role clientRole = roleRepository.findRoleByNameIgnoreCase("User");
        User user = userRepository.findUserByEmail(userEmail);
        if (userEmail.contains("@friendlybank.com")) {
            user.setRole(managerRole);
        } else {
            user.setRole(clientRole);
        }
        userRepository.save(user);
        return user;
    }

    // Fetch list of users by theirs roles
    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findUserByRole(role);
    }

    // Fetch list of users by theirs role's name
    @Override
    public List<User> getUserByRoleName(String roleName) {
        List<User> users = userRepository.findUserByRoleName(roleName);
        return users;
    }

    // Finding user by its email
    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail);
    }


}
