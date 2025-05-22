package com.phone.controller;

import com.phone.model.Phone;
import com.phone.model.SystemUser;
import com.phone.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/phones")
@CrossOrigin(origins = "http://localhost:5173")
public class PhoneController {
    @Autowired
    private PhoneService phoneService;

    @GetMapping
    public List<Phone> getAllPhones() {
        return phoneService.getAllPhones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Phone> getPhoneById(@PathVariable Long id) {
        return phoneService.getPhoneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brand/{brand}")
    public List<Phone> getPhonesByBrand(@PathVariable String brand) {
        return phoneService.getPhonesByBrand(brand);
    }

    @GetMapping("/model/{model}")
    public List<Phone> getPhonesByModel(@PathVariable String model) {
        return phoneService.getPhonesByModel(model);
    }

    @GetMapping("/active")
    public List<Phone> getActivePhones() {
        return phoneService.getActivePhones();
    }

    @GetMapping("/inactive")
    public List<Phone> getInactivePhones() {
        return phoneService.getInactivePhones();
    }

    @PostMapping
    public Phone createPhone(@RequestBody Phone phone, @RequestAttribute("currentUser") SystemUser currentUser) {
        return phoneService.createPhone(phone, currentUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Phone> updatePhone(@PathVariable Long id, @RequestBody Phone phone) {
        try {
            Phone updatedPhone = phoneService.updatePhone(id, phone);
            return ResponseEntity.ok(updatedPhone);
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
    public ResponseEntity<Phone> deactivatePhone(@PathVariable Long id) {
        try {
            Phone deactivatedPhone = phoneService.deactivatePhone(id);
            return ResponseEntity.ok(deactivatedPhone);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Phone> activatePhone(@PathVariable Long id) {
        try {
            Phone activatedPhone = phoneService.activatePhone(id);
            return ResponseEntity.ok(activatedPhone);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 