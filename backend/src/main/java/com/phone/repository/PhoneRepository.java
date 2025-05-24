package com.phone.repository;

import com.phone.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByBrand(String brand);
    List<Phone> findByModel(String model);
    List<Phone> findByIsActive(boolean isActive);
    List<Phone> findByBrandAndModel(String brand, String model);
    List<Phone> findByDeletedFalse();
} 