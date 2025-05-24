package com.phone.repository;

import com.phone.model.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SimCardRepository extends JpaRepository<SimCard, Long> {
    Optional<SimCard> findByNumber(String number);
    List<SimCard> findByOperator(String operator);
    List<SimCard> findByForfait(String forfait);
    List<SimCard> findByIsAssigned(boolean isAssigned);
    List<SimCard> findByOperatorAndForfait(String operator, String forfait);
    List<SimCard> findByDeletedFalse();
} 