package com.phone.service;

import com.phone.model.Phone;
import com.phone.model.SystemUser;
import com.phone.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PhoneService {
    @Autowired
    private PhoneRepository phoneRepository;

    public List<Phone> getAllPhones() {
        return phoneRepository.findAll();
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
        phoneRepository.deleteById(id);
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
} 