package com.phone.service;

import com.phone.model.SystemUser;
import com.phone.model.Role;
import com.phone.dto.CreateSystemUserDto;
import com.phone.dto.UpdateSystemUserDto;
import com.phone.repository.SystemUserRepository;
import com.phone.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SystemUserService {
    
    @Autowired
    private SystemUserRepository systemUserRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<SystemUser> getAllSystemUsers() {
        return systemUserRepository.findByDeletedFalse();
    }
    
    public Optional<SystemUser> getSystemUserById(Long id) {
        return systemUserRepository.findById(id);
    }
    
    public Optional<SystemUser> getSystemUserByUsername(String username) {
        return systemUserRepository.findByUsername(username);
    }
    
    public SystemUser createSystemUser(CreateSystemUserDto createDto) {
        // Validate that username is not already taken
        if (systemUserRepository.findByUsername(createDto.username).isPresent()) {
            throw new RuntimeException("Username already exists: " + createDto.username);
        }
        
        // Validate that email is not already taken
        if (systemUserRepository.findByEmail(createDto.email).isPresent()) {
            throw new RuntimeException("Email already exists: " + createDto.email);
        }
        
        // Find the role
        Role role = roleRepository.findById(createDto.roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + createDto.roleId));
        
        // Create new system user
        SystemUser systemUser = new SystemUser();
        systemUser.setUsername(createDto.username);
        systemUser.setEmail(createDto.email);
        systemUser.setPasswordHash(passwordEncoder.encode(createDto.password));
        systemUser.setRole(role);
        
        return systemUserRepository.save(systemUser);
    }
    
    public SystemUser updateSystemUser(Long id, UpdateSystemUserDto updateDto) {
        return systemUserRepository.findById(id)
                .map(existingUser -> {
                    // Update username if provided and not already taken
                    if (updateDto.username != null && !updateDto.username.equals(existingUser.getUsername())) {
                        if (systemUserRepository.findByUsername(updateDto.username).isPresent()) {
                            throw new RuntimeException("Username already exists: " + updateDto.username);
                        }
                        existingUser.setUsername(updateDto.username);
                    }
                    
                    // Update email if provided and not already taken
                    if (updateDto.email != null && !updateDto.email.equals(existingUser.getEmail())) {
                        if (systemUserRepository.findByEmail(updateDto.email).isPresent()) {
                            throw new RuntimeException("Email already exists: " + updateDto.email);
                        }
                        existingUser.setEmail(updateDto.email);
                    }
                    
                    // Update password if provided
                    if (updateDto.password != null && !updateDto.password.trim().isEmpty()) {
                        existingUser.setPasswordHash(passwordEncoder.encode(updateDto.password));
                    }
                    
                    // Update role if provided
                    if (updateDto.roleId != null) {
                        Role role = roleRepository.findById(updateDto.roleId)
                                .orElseThrow(() -> new RuntimeException("Role not found with id: " + updateDto.roleId));
                        existingUser.setRole(role);
                    }
                    
                    return systemUserRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("System user not found with id: " + id));
    }
    
    public SystemUser softDeleteSystemUser(Long id) {
        return systemUserRepository.findById(id)
                .map(user -> {
                    user.setDeleted(true);
                    return systemUserRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("System user not found with id: " + id));
    }
    
    public static com.phone.dto.SystemUserDto toDto(SystemUser user) {
        if (user == null) return null;
        com.phone.dto.SystemUserDto dto = new com.phone.dto.SystemUserDto();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.role = user.getRole() != null ? user.getRole().getName().name() : null;
        return dto;
    }
    
    public static java.util.List<com.phone.dto.SystemUserDto> toDtoList(java.util.List<SystemUser> users) {
        java.util.List<com.phone.dto.SystemUserDto> list = new java.util.ArrayList<>();
        for (SystemUser u : users) {
            list.add(toDto(u));
        }
        return list;
    }
} 