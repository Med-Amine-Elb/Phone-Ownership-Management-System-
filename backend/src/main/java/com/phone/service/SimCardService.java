package com.phone.service;

import com.phone.model.SimCard;
import com.phone.model.SystemUser;
import com.phone.repository.SimCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class SimCardService {
    @Autowired
    private SimCardRepository simCardRepository;

    public List<SimCard> getAllSimCards() {
        return simCardRepository.findAll();
    }

    public Page<SimCard> getSimCards(Pageable pageable, String filterField, String filterValue) {
        if (filterField != null && filterValue != null && !filterValue.isEmpty()) {
            switch (filterField) {
                case "number":
                    return simCardRepository.findByNumberContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "operator":
                    return simCardRepository.findByOperatorContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "forfait":
                    return simCardRepository.findByForfaitContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "assigned":
                    try {
                        boolean isAssigned = Boolean.parseBoolean(filterValue);
                        return simCardRepository.findByIsAssignedAndDeletedFalse(isAssigned, pageable);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid boolean value for assigned filter
                        return Page.empty(pageable);
                    }
                // Add more filter cases as needed
                default:
                    // No valid filter field provided, return all non-deleted sim cards with pagination/sorting
                    return simCardRepository.findByDeletedFalse(pageable);
            }
        } else {
            // No filter provided, return all non-deleted sim cards with pagination/sorting
            return simCardRepository.findByDeletedFalse(pageable);
        }
    }

    public Optional<SimCard> getSimCardById(Long id) {
        return simCardRepository.findById(id);
    }

    public Optional<SimCard> getSimCardByNumber(String number) {
        return simCardRepository.findByNumber(number);
    }

    public List<SimCard> getSimCardsByOperator(String operator) {
        return simCardRepository.findByOperator(operator);
    }

    public List<SimCard> getSimCardsByForfait(String forfait) {
        return simCardRepository.findByForfait(forfait);
    }

    public List<SimCard> getAssignedSimCards() {
        return simCardRepository.findByIsAssigned(true);
    }

    public List<SimCard> getUnassignedSimCards() {
        return simCardRepository.findByIsAssigned(false);
    }

    public SimCard createSimCard(SimCard simCard, SystemUser createdBy) {
        simCard.setCreatedBy(createdBy);
        return simCardRepository.save(simCard);
    }

    public SimCard updateSimCard(Long id, SimCard simCardDetails) {
        return simCardRepository.findById(id)
                .map(existingSimCard -> {
                    existingSimCard.setNumber(simCardDetails.getNumber());
                    existingSimCard.setSsid(simCardDetails.getSsid());
                    existingSimCard.setPinCode(simCardDetails.getPinCode());
                    existingSimCard.setPukCode(simCardDetails.getPukCode());
                    existingSimCard.setForfait(simCardDetails.getForfait());
                    existingSimCard.setOperator(simCardDetails.getOperator());
                    existingSimCard.setAssigned(simCardDetails.isAssigned());
                    return simCardRepository.save(existingSimCard);
                })
                .orElseThrow(() -> new RuntimeException("SIM card not found with id: " + id));
    }

    public void deleteSimCard(Long id) {
        simCardRepository.findById(id).ifPresent(simCard -> {
            simCard.setDeleted(true);
            simCardRepository.save(simCard);
        });
    }

    public SimCard markAsAssigned(Long id) {
        return simCardRepository.findById(id)
                .map(simCard -> {
                    simCard.setAssigned(true);
                    return simCardRepository.save(simCard);
                })
                .orElseThrow(() -> new RuntimeException("SIM card not found with id: " + id));
    }

    public SimCard markAsUnassigned(Long id) {
        return simCardRepository.findById(id)
                .map(simCard -> {
                    simCard.setAssigned(false);
                    return simCardRepository.save(simCard);
                })
                .orElseThrow(() -> new RuntimeException("SIM card not found with id: " + id));
    }
} 