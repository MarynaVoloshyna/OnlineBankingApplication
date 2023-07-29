package com.voloshyna.onlinebankingapplication.service.interf;

import com.voloshyna.onlinebankingapplication.entity.Role;
import com.voloshyna.onlinebankingapplication.entity.User;

import java.util.List;

public interface UserService {
    User registrateUser(String userEmail, String userPassword);
    User setRoleToUser(String userEmail);
    List<User> getUsersByRole(Role role);
    List<User> getUserByRoleName(String roleName);
    User getUserByEmail(String userEmail);


}
