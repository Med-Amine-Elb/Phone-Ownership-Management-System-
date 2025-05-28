package com.phone.controller;

import com.phone.model.Role;
import com.phone.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public List<com.phone.dto.RoleDto> getAllRoles() {
        return com.phone.model.Role.toDtoList(roleRepository.findByDeletedFalse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.RoleDto> getRoleById(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(r -> ResponseEntity.ok(com.phone.model.Role.toDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public com.phone.dto.RoleDto createRole(@RequestBody com.phone.model.Role role) {
        return com.phone.model.Role.toDto(roleRepository.save(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.RoleDto> updateRole(@PathVariable Long id, @RequestBody com.phone.model.Role roleDetails) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(roleDetails.getName());
                    return ResponseEntity.ok(com.phone.model.Role.toDto(roleRepository.save(role)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        return roleRepository.findById(id)
            .map(role -> {
                role.setDeleted(true);
                roleRepository.save(role);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 