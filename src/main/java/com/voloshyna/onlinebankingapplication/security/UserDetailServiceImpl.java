package com.voloshyna.onlinebankingapplication.security;

import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
@Lazy
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found!");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getName());
        authorities.add(simpleGrantedAuthority);
        org.springframework.security.core.userdetails.User userDetails
                = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        return userDetails;
    }
}
