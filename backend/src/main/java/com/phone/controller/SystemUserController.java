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
    public List<SystemUser> getAllSystemUsers() {
        return systemUserRepository.findByDeletedFalse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemUser> getSystemUserById(@PathVariable Long id) {
        return systemUserRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SystemUser createSystemUser(@RequestBody SystemUser systemUser) {
        return systemUserRepository.save(systemUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SystemUser> updateSystemUser(@PathVariable Long id, @RequestBody SystemUser userDetails) {
        return systemUserRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPasswordHash(userDetails.getPasswordHash());
                    user.setRole(userDetails.getRole());
                    return ResponseEntity.ok(systemUserRepository.save(user));
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