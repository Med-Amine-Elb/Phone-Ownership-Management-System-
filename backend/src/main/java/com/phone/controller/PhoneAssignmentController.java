package com.phone.controller;

import com.phone.model.PhoneAssignment;
import com.phone.model.SystemUser;
import com.phone.service.PhoneAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.phone.repository.SystemUserRepository;
import com.phone.repository.PhoneAssignmentRepository;
import com.phone.repository.AssignmentHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "http://localhost:5173")
public class PhoneAssignmentController {
    @Autowired
    private PhoneAssignmentService phoneAssignmentService;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @GetMapping
    public ResponseEntity<Page<com.phone.dto.PhoneAssignmentDto>> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<PhoneAssignment> assignmentsPage = phoneAssignmentService.getAssignments(
                pageable, filterField, filterValue);

        return ResponseEntity.ok(assignmentsPage.map(PhoneAssignment::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.PhoneAssignmentDto> getAssignmentById(@PathVariable Long id) {
        return phoneAssignmentService.getAssignmentById(id)
                .map(a -> ResponseEntity.ok(com.phone.service.PhoneAssignmentService.toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<com.phone.dto.PhoneAssignmentDto> getAssignmentsByUserId(@PathVariable Long userId) {
        return com.phone.service.PhoneAssignmentService.toDtoList(phoneAssignmentService.getAssignmentsByUserId(userId));
    }

    @GetMapping("/phone/{phoneId}")
    public List<com.phone.dto.PhoneAssignmentDto> getAssignmentsByPhoneId(@PathVariable Long phoneId) {
        return com.phone.service.PhoneAssignmentService.toDtoList(phoneAssignmentService.getAssignmentsByPhoneId(phoneId));
    }

    @GetMapping("/sim-card/{simCardId}")
    public List<com.phone.dto.PhoneAssignmentDto> getAssignmentsBySimCardId(@PathVariable Long simCardId) {
        return com.phone.service.PhoneAssignmentService.toDtoList(phoneAssignmentService.getAssignmentsBySimCardId(simCardId));
    }

    @GetMapping("/active")
    public List<com.phone.dto.PhoneAssignmentDto> getActiveAssignments() {
        return com.phone.service.PhoneAssignmentService.toDtoList(phoneAssignmentService.getActiveAssignments());
    }

    @GetMapping("/returned")
    public List<com.phone.dto.PhoneAssignmentDto> getReturnedAssignments() {
        return com.phone.service.PhoneAssignmentService.toDtoList(phoneAssignmentService.getReturnedAssignments());
    }

    @GetMapping("/upcoming-upgrades")
    public List<com.phone.dto.PhoneAssignmentDto> getUpcomingUpgrades() {
        return com.phone.service.PhoneAssignmentService.toDtoList(phoneAssignmentService.getUpcomingUpgrades());
    }

    @PostMapping
    public ResponseEntity<com.phone.dto.PhoneAssignmentDto> createAssignment(@RequestBody PhoneAssignment assignment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SystemUser user = systemUserRepository.findByUsername(username).orElse(null);
        assignment.setAssignedBy(user);
        try {
            PhoneAssignment createdAssignment = phoneAssignmentService.createAssignment(assignment);
            return ResponseEntity.ok(com.phone.service.PhoneAssignmentService.toDto(createdAssignment));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<com.phone.dto.PhoneAssignmentDto> returnAssignment(@PathVariable Long id) {
        try {
            PhoneAssignment returnedAssignment = phoneAssignmentService.returnAssignment(id);
            return ResponseEntity.ok(com.phone.service.PhoneAssignmentService.toDto(returnedAssignment));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.PhoneAssignmentDto> updateAssignment(@PathVariable Long id, @RequestBody PhoneAssignment assignment) {
        try {
            PhoneAssignment updatedAssignment = phoneAssignmentService.updateAssignment(id, assignment);
            return ResponseEntity.ok(com.phone.service.PhoneAssignmentService.toDto(updatedAssignment));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<com.phone.dto.PhoneAssignmentDto> softDeleteAssignment(@PathVariable Long id) {
        try {
            PhoneAssignment deletedAssignment = phoneAssignmentService.softDelete(id);
            return ResponseEntity.ok(com.phone.service.PhoneAssignmentService.toDto(deletedAssignment));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 