package com.voloshyna.onlinebankingapplication.repository;

import com.voloshyna.onlinebankingapplication.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.voloshyna.onlinebankingapplication.entity.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    //List<User> findUserByRoles(Set<Role> roles);
    @EntityGraph(attributePaths = {"roles"})
    List<User> findAll();
    List<User> findUserByRole(Role role);
    List<User> findUserByRoleName(String roleName);


}
