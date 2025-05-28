package com.phone.controller;

import com.phone.model.SystemUser;
import com.phone.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/system-users")
@CrossOrigin(origins = "http://localhost:5173")
public class SystemUserController {
    @Autowired
    private SystemUserRepository systemUserRepository;

    @GetMapping
    public List<com.phone.dto.SystemUserDto> getAllSystemUsers() {
        return SystemUser.toDtoList(systemUserRepository.findByDeletedFalse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.SystemUserDto> getSystemUserById(@PathVariable Long id) {
        return systemUserRepository.findById(id)
                .map(u -> ResponseEntity.ok(SystemUser.toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public com.phone.dto.SystemUserDto createSystemUser(@RequestBody com.phone.model.SystemUser systemUser) {
        return SystemUser.toDto(systemUserRepository.save(systemUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.SystemUserDto> updateSystemUser(@PathVariable Long id, @RequestBody com.phone.model.SystemUser userDetails) {
        return systemUserRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPasswordHash(userDetails.getPasswordHash());
                    user.setRole(userDetails.getRole());
                    return ResponseEntity.ok(SystemUser.toDto(systemUserRepository.save(user)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemUser(@PathVariable Long id) {
        return systemUserRepository.findById(id)
            .map(user -> {
                user.setDeleted(true);
                systemUserRepository.save(user);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 