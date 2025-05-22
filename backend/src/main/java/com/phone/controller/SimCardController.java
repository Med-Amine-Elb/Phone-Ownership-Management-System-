package com.phone.controller;

import com.phone.model.SimCard;
import com.phone.model.SystemUser;
import com.phone.service.SimCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sim-cards")
@CrossOrigin(origins = "http://localhost:5173")
public class SimCardController {
    @Autowired
    private SimCardService simCardService;

    @GetMapping
    public List<SimCard> getAllSimCards() {
        return simCardService.getAllSimCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimCard> getSimCardById(@PathVariable Long id) {
        return simCardService.getSimCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<SimCard> getSimCardByNumber(@PathVariable String number) {
        return simCardService.getSimCardByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/operator/{operator}")
    public List<SimCard> getSimCardsByOperator(@PathVariable String operator) {
        return simCardService.getSimCardsByOperator(operator);
    }

    @GetMapping("/forfait/{forfait}")
    public List<SimCard> getSimCardsByForfait(@PathVariable String forfait) {
        return simCardService.getSimCardsByForfait(forfait);
    }

    @GetMapping("/assigned")
    public List<SimCard> getAssignedSimCards() {
        return simCardService.getAssignedSimCards();
    }

    @GetMapping("/unassigned")
    public List<SimCard> getUnassignedSimCards() {
        return simCardService.getUnassignedSimCards();
    }

    @PostMapping
    public SimCard createSimCard(@RequestBody SimCard simCard, @RequestAttribute("currentUser") SystemUser currentUser) {
        return simCardService.createSimCard(simCard, currentUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimCard> updateSimCard(@PathVariable Long id, @RequestBody SimCard simCard) {
        try {
            SimCard updatedSimCard = simCardService.updateSimCard(id, simCard);
            return ResponseEntity.ok(updatedSimCard);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSimCard(@PathVariable Long id) {
        try {
            simCardService.deleteSimCard(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/mark-assigned")
    public ResponseEntity<SimCard> markAsAssigned(@PathVariable Long id) {
        try {
            SimCard updatedSimCard = simCardService.markAsAssigned(id);
            return ResponseEntity.ok(updatedSimCard);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/mark-unassigned")
    public ResponseEntity<SimCard> markAsUnassigned(@PathVariable Long id) {
        try {
            SimCard updatedSimCard = simCardService.markAsUnassigned(id);
            return ResponseEntity.ok(updatedSimCard);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 