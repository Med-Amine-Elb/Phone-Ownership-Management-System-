package com.phone.controller;

import com.phone.model.SimCard;
import com.phone.model.SystemUser;
import com.phone.service.SimCardService;
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
@RequestMapping("/api/sim-cards")
@CrossOrigin(origins = "http://localhost:5173")
public class SimCardController {
    @Autowired
    private SimCardService simCardService;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @GetMapping
    public ResponseEntity<Page<com.phone.dto.SimCardDto>> getAllSimCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<SimCard> simCardsPage = simCardService.getSimCards(
                pageable, filterField, filterValue);

        return ResponseEntity.ok(simCardsPage.map(SimCard::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.SimCardDto> getSimCardById(@PathVariable Long id) {
        return simCardService.getSimCardById(id)
                .map(s -> ResponseEntity.ok(SimCard.toDto(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<com.phone.dto.SimCardDto> getSimCardByNumber(@PathVariable String number) {
        return simCardService.getSimCardByNumber(number)
                .map(s -> ResponseEntity.ok(SimCard.toDto(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/operator/{operator}")
    public List<com.phone.dto.SimCardDto> getSimCardsByOperator(@PathVariable String operator) {
        return SimCard.toDtoList(simCardService.getSimCardsByOperator(operator));
    }

    @GetMapping("/forfait/{forfait}")
    public List<com.phone.dto.SimCardDto> getSimCardsByForfait(@PathVariable String forfait) {
        return SimCard.toDtoList(simCardService.getSimCardsByForfait(forfait));
    }

    @GetMapping("/assigned")
    public List<com.phone.dto.SimCardDto> getAssignedSimCards() {
        return SimCard.toDtoList(simCardService.getAssignedSimCards());
    }

    @GetMapping("/unassigned")
    public List<com.phone.dto.SimCardDto> getUnassignedSimCards() {
        return SimCard.toDtoList(simCardService.getUnassignedSimCards());
    }

    @PostMapping
    public com.phone.dto.SimCardDto createSimCard(@RequestBody com.phone.model.SimCard simCard) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SystemUser user = systemUserRepository.findByUsername(username).orElse(null);
        return SimCard.toDto(simCardService.createSimCard(simCard, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.SimCardDto> updateSimCard(@PathVariable Long id, @RequestBody com.phone.model.SimCard simCard) {
        try {
            com.phone.model.SimCard updatedSimCard = simCardService.updateSimCard(id, simCard);
            return ResponseEntity.ok(SimCard.toDto(updatedSimCard));
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
    public ResponseEntity<com.phone.dto.SimCardDto> markAsAssigned(@PathVariable Long id) {
        try {
            com.phone.model.SimCard updatedSimCard = simCardService.markAsAssigned(id);
            return ResponseEntity.ok(SimCard.toDto(updatedSimCard));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/mark-unassigned")
    public ResponseEntity<com.phone.dto.SimCardDto> markAsUnassigned(@PathVariable Long id) {
        try {
            com.phone.model.SimCard updatedSimCard = simCardService.markAsUnassigned(id);
            return ResponseEntity.ok(SimCard.toDto(updatedSimCard));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 