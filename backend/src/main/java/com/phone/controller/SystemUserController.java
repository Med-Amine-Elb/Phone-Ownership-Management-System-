package com.phone.controller;

import com.phone.model.SystemUser;
import com.phone.service.SystemUserService;
import com.phone.dto.CreateSystemUserDto;
import com.phone.dto.UpdateSystemUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.phone.repository.RoleRepository;

@RestController
@RequestMapping("/api/system-users")
@CrossOrigin(origins = "http://localhost:5173")
public class SystemUserController {
    
    private static final Logger logger = LoggerFactory.getLogger(SystemUserController.class);
    
    @Autowired
    private SystemUserService systemUserService;
    
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public List<com.phone.dto.SystemUserDto> getAllSystemUsers() {
        return SystemUserService.toDtoList(systemUserService.getAllSystemUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.SystemUserDto> getSystemUserById(@PathVariable Long id) {
        return systemUserService.getSystemUserById(id)
                .map(u -> ResponseEntity.ok(SystemUserService.toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/debug/roles")
    public ResponseEntity<String> debugRoles() {
        try {
            logger.info("Debugging roles...");
            var roles = roleRepository.findAll();
            logger.info("Found {} roles", roles.size());
            for (var role : roles) {
                logger.info("Role ID: {}, Name: {}", role.getId(), role.getName());
            }
            return ResponseEntity.ok("Roles debugged successfully. Check logs.");
        } catch (Exception e) {
            logger.error("Error debugging roles: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<com.phone.dto.SystemUserDto> createSystemUser(@RequestBody CreateSystemUserDto createDto) {
        try {
            logger.info("Creating new system user: {}", createDto.username);
            logger.info("Role ID requested: {}", createDto.roleId);
            
            // Debug role lookup
            var role = roleRepository.findById(createDto.roleId);
            if (role.isPresent()) {
                logger.info("Role found: {}", role.get().getName());
            } else {
                logger.error("Role not found with ID: {}", createDto.roleId);
                return ResponseEntity.badRequest().build();
            }
            
            SystemUser createdUser = systemUserService.createSystemUser(createDto);
            logger.info("System user created successfully with ID: {}", createdUser.getId());
            return ResponseEntity.ok(SystemUserService.toDto(createdUser));
        } catch (RuntimeException e) {
            logger.error("Error creating system user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error creating system user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.SystemUserDto> updateSystemUser(@PathVariable Long id, @RequestBody UpdateSystemUserDto updateDto) {
        try {
            logger.info("Updating system user with ID: {}", id);
            SystemUser updatedUser = systemUserService.updateSystemUser(id, updateDto);
            logger.info("System user updated successfully");
            return ResponseEntity.ok(SystemUserService.toDto(updatedUser));
        } catch (RuntimeException e) {
            logger.error("Error updating system user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error updating system user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemUser(@PathVariable Long id) {
        try {
            logger.info("Deleting system user with ID: {}", id);
            systemUserService.softDeleteSystemUser(id);
            logger.info("System user deleted successfully");
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting system user: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error deleting system user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 