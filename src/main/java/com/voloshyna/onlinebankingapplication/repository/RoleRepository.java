package com.voloshyna.onlinebankingapplication.repository;

import com.voloshyna.onlinebankingapplication.entity.Role;
import com.voloshyna.onlinebankingapplication.entity.User;
import jakarta.persistence.FetchType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);
    Role findRoleByNameIgnoreCase(String name);
}
