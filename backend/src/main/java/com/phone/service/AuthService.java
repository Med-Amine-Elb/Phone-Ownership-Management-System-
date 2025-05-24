package com.phone.service;

import com.phone.model.SystemUser;
import com.phone.repository.SystemUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);
        SystemUser systemUser = systemUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        logger.info("User found: {} with role {} (deleted: {})", systemUser.getUsername(), systemUser.getRole().getName(), systemUser.isDeleted());
        return new User(
            systemUser.getUsername(),
            systemUser.getPasswordHash(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + systemUser.getRole().getName().name()))
        );
    }
} 