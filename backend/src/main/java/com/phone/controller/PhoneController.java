package com.phone.controller;

import com.phone.model.Phone;
import com.phone.model.SystemUser;
import com.phone.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.phone.repository.SystemUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/phones")
@CrossOrigin(origins = "http://localhost:5173")
public class PhoneController {
    @Autowired
    private PhoneService phoneService;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @GetMapping
    public ResponseEntity<Page<com.phone.dto.PhoneDto>> getAllPhones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<Phone> phonesPage = phoneService.getPhones(
                pageable, filterField, filterValue);

        return ResponseEntity.ok(phonesPage.map(Phone::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.PhoneDto> getPhoneById(@PathVariable Long id) {
        return phoneService.getPhoneById(id)
                .map(p -> ResponseEntity.ok(com.phone.service.PhoneService.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brand/{brand}")
    public List<com.phone.dto.PhoneDto> getPhonesByBrand(@PathVariable String brand) {
        return com.phone.service.PhoneService.toDtoList(phoneService.getPhonesByBrand(brand));
    }

    @GetMapping("/model/{model}")
    public List<com.phone.dto.PhoneDto> getPhonesByModel(@PathVariable String model) {
        return com.phone.service.PhoneService.toDtoList(phoneService.getPhonesByModel(model));
    }

    @GetMapping("/active")
    public List<com.phone.dto.PhoneDto> getActivePhones() {
        return com.phone.service.PhoneService.toDtoList(phoneService.getActivePhones());
    }

    @GetMapping("/inactive")
    public List<com.phone.dto.PhoneDto> getInactivePhones() {
        return com.phone.service.PhoneService.toDtoList(phoneService.getInactivePhones());
    }

    @PostMapping
    public com.phone.dto.PhoneDto createPhone(@RequestBody com.phone.model.Phone phone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SystemUser user = systemUserRepository.findByUsername(username).orElse(null);
        return com.phone.service.PhoneService.toDto(phoneService.createPhone(phone, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.PhoneDto> updatePhone(@PathVariable Long id, @RequestBody com.phone.model.Phone phone) {
        try {
            com.phone.model.Phone updatedPhone = phoneService.updatePhone(id, phone);
            return ResponseEntity.ok(com.phone.service.PhoneService.toDto(updatedPhone));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
        try {
            phoneService.deletePhone(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<com.phone.dto.PhoneDto> deactivatePhone(@PathVariable Long id) {
        try {
            com.phone.model.Phone deactivatedPhone = phoneService.deactivatePhone(id);
            return ResponseEntity.ok(com.phone.service.PhoneService.toDto(deactivatedPhone));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<com.phone.dto.PhoneDto> activatePhone(@PathVariable Long id) {
        try {
            com.phone.model.Phone activatedPhone = phoneService.activatePhone(id);
            return ResponseEntity.ok(com.phone.service.PhoneService.toDto(activatedPhone));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 