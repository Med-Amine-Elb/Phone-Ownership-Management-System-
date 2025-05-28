package com.phone.repository;

import com.phone.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByBrand(String brand);
    List<Phone> findByModel(String model);
    List<Phone> findByIsActive(boolean isActive);
    List<Phone> findByBrandAndModel(String brand, String model);
    List<Phone> findByDeletedFalse();

    // Methods for pagination, sorting, and filtering
    Page<Phone> findByDeletedFalse(Pageable pageable);
    Page<Phone> findByBrandContainingIgnoreCaseAndDeletedFalse(String brand, Pageable pageable);
    Page<Phone> findByModelContainingIgnoreCaseAndDeletedFalse(String model, Pageable pageable);
    Page<Phone> findByIsActiveAndDeletedFalse(boolean isActive, Pageable pageable);
} 