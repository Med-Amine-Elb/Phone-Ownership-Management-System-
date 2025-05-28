package com.phone.repository;

import com.phone.model.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface SimCardRepository extends JpaRepository<SimCard, Long> {
    Optional<SimCard> findByNumber(String number);
    List<SimCard> findByOperator(String operator);
    List<SimCard> findByForfait(String forfait);
    List<SimCard> findByIsAssigned(boolean isAssigned);
    List<SimCard> findByOperatorAndForfait(String operator, String forfait);
    List<SimCard> findByDeletedFalse();

    // Methods for pagination, sorting, and filtering
    Page<SimCard> findByDeletedFalse(Pageable pageable);
    Page<SimCard> findByNumberContainingIgnoreCaseAndDeletedFalse(String number, Pageable pageable);
    Page<SimCard> findByOperatorContainingIgnoreCaseAndDeletedFalse(String operator, Pageable pageable);
    Page<SimCard> findByForfaitContainingIgnoreCaseAndDeletedFalse(String forfait, Pageable pageable);
    Page<SimCard> findByIsAssignedAndDeletedFalse(boolean isAssigned, Pageable pageable);
} 