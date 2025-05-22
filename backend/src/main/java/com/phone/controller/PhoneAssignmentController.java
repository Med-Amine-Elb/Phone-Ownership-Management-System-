package com.phone.controller;

import com.phone.model.PhoneAssignment;
import com.phone.model.SystemUser;
import com.phone.service.PhoneAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "http://localhost:5173")
public class PhoneAssignmentController {
    @Autowired
    private PhoneAssignmentService phoneAssignmentService;

    @GetMapping
    public List<PhoneAssignment> getAllAssignments() {
        return phoneAssignmentService.getAllAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneAssignment> getAssignmentById(@PathVariable Long id) {
        return phoneAssignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<PhoneAssignment> getAssignmentsByUserId(@PathVariable Long userId) {
        return phoneAssignmentService.getAssignmentsByUserId(userId);
    }

    @GetMapping("/phone/{phoneId}")
    public List<PhoneAssignment> getAssignmentsByPhoneId(@PathVariable Long phoneId) {
        return phoneAssignmentService.getAssignmentsByPhoneId(phoneId);
    }

    @GetMapping("/sim-card/{simCardId}")
    public List<PhoneAssignment> getAssignmentsBySimCardId(@PathVariable Long simCardId) {
        return phoneAssignmentService.getAssignmentsBySimCardId(simCardId);
    }

    @GetMapping("/active")
    public List<PhoneAssignment> getActiveAssignments() {
        return phoneAssignmentService.getActiveAssignments();
    }

    @GetMapping("/returned")
    public List<PhoneAssignment> getReturnedAssignments() {
        return phoneAssignmentService.getReturnedAssignments();
    }

    @GetMapping("/upcoming-upgrades")
    public List<PhoneAssignment> getUpcomingUpgrades() {
        return phoneAssignmentService.getUpcomingUpgrades();
    }

    @PostMapping
    public ResponseEntity<PhoneAssignment> createAssignment(
            @RequestBody PhoneAssignment assignment,
            @RequestAttribute("currentUser") SystemUser currentUser) {
        try {
            PhoneAssignment createdAssignment = phoneAssignmentService.createAssignment(assignment, currentUser);
            return ResponseEntity.ok(createdAssignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<PhoneAssignment> returnAssignment(@PathVariable Long id) {
        try {
            PhoneAssignment returnedAssignment = phoneAssignmentService.returnAssignment(id);
            return ResponseEntity.ok(returnedAssignment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 