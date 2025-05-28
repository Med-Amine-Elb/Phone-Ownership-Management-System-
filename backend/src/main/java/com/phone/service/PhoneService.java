package com.phone.service;

import com.phone.model.Phone;
import com.phone.model.SystemUser;
import com.phone.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class PhoneService {
    @Autowired
    private PhoneRepository phoneRepository;

    public List<Phone> getAllPhones() {
        return phoneRepository.findByDeletedFalse();
    }

    public Page<Phone> getPhones(Pageable pageable, String filterField, String filterValue) {
        if (filterField != null && filterValue != null && !filterValue.isEmpty()) {
            switch (filterField) {
                case "brand":
                    return phoneRepository.findByBrandContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "model":
                    return phoneRepository.findByModelContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "active":
                    try {
                        boolean isActive = Boolean.parseBoolean(filterValue);
                        return phoneRepository.findByIsActiveAndDeletedFalse(isActive, pageable);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid boolean value for active filter
                        return Page.empty(pageable);
                    }
                // Add more filter cases as needed
                default:
                    // No valid filter field provided, return all non-deleted phones with pagination/sorting
                    return phoneRepository.findByDeletedFalse(pageable);
            }
        } else {
            // No filter provided, return all non-deleted phones with pagination/sorting
            return phoneRepository.findByDeletedFalse(pageable);
        }
    }

    public Optional<Phone> getPhoneById(Long id) {
        return phoneRepository.findById(id);
    }

    public List<Phone> getPhonesByBrand(String brand) {
        return phoneRepository.findByBrand(brand);
    }

    public List<Phone> getPhonesByModel(String model) {
        return phoneRepository.findByModel(model);
    }

    public List<Phone> getActivePhones() {
        return phoneRepository.findByIsActive(true);
    }

    public List<Phone> getInactivePhones() {
        return phoneRepository.findByIsActive(false);
    }

    public Phone createPhone(Phone phone, SystemUser createdBy) {
        phone.setCreatedBy(createdBy);
        return phoneRepository.save(phone);
    }

    public Phone updatePhone(Long id, Phone phoneDetails) {
        return phoneRepository.findById(id)
                .map(existingPhone -> {
                    existingPhone.setBrand(phoneDetails.getBrand());
                    existingPhone.setModel(phoneDetails.getModel());
                    existingPhone.setActive(phoneDetails.isActive());
                    return phoneRepository.save(existingPhone);
                })
                .orElseThrow(() -> new RuntimeException("Phone not found with id: " + id));
    }

    public void deletePhone(Long id) {
        phoneRepository.findById(id).ifPresent(phone -> {
            phone.setDeleted(true);
            phoneRepository.save(phone);
        });
    }

    public Phone deactivatePhone(Long id) {
        return phoneRepository.findById(id)
                .map(phone -> {
                    phone.setActive(false);
                    return phoneRepository.save(phone);
                })
                .orElseThrow(() -> new RuntimeException("Phone not found with id: " + id));
    }

    public Phone activatePhone(Long id) {
        return phoneRepository.findById(id)
                .map(phone -> {
                    phone.setActive(true);
                    return phoneRepository.save(phone);
                })
                .orElseThrow(() -> new RuntimeException("Phone not found with id: " + id));
    }

    public static com.phone.dto.PhoneDto toDto(com.phone.model.Phone phone) {
        if (phone == null) return null;
        com.phone.dto.PhoneDto dto = new com.phone.dto.PhoneDto();
        dto.id = phone.getId();
        dto.brand = phone.getBrand();
        dto.model = phone.getModel();
        dto.active = phone.isActive();
        return dto;
    }

    public static java.util.List<com.phone.dto.PhoneDto> toDtoList(java.util.List<com.phone.model.Phone> phones) {
        java.util.List<com.phone.dto.PhoneDto> list = new java.util.ArrayList<>();
        for (com.phone.model.Phone p : phones) {
            list.add(toDto(p));
        }
        return list;
    }
} 