package com.phone.service;

import com.phone.model.AssignmentHistory;
import com.phone.repository.AssignmentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@Transactional
public class AssignmentHistoryService {

    @Autowired
    private AssignmentHistoryRepository assignmentHistoryRepository;

    public Page<AssignmentHistory> getAssignmentHistory(Pageable pageable, String filterField, String filterValue) {
        Specification<AssignmentHistory> spec = Specification.where(null);

        if (filterField != null && filterValue != null && !filterValue.isEmpty()) {
            switch (filterField) {
                case "user":
                     try {
                        Long userId = Long.parseLong(filterValue);
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
                    } catch (NumberFormatException e) {
                        return Page.empty(pageable);
                    }
                    break;
                case "phone":
                    try {
                        Long phoneId = Long.parseLong(filterValue);
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("phone").get("id"), phoneId));
                    } catch (NumberFormatException e) {
                        return Page.empty(pageable);
                    }
                    break;
                case "simCard":
                     try {
                        Long simCardId = Long.parseLong(filterValue);
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("simCard").get("id"), simCardId));
                    } catch (NumberFormatException e) {
                        return Page.empty(pageable);
                    }
                    break;
                 case "assignedBy":
                     try {
                        Long assignedById = Long.parseLong(filterValue);
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("assignedBy").get("id"), assignedById));
                    } catch (NumberFormatException e) {
                        return Page.empty(pageable);
                    }
                    break;
                case "status":
                    try {
                         AssignmentHistory.HistoryStatus status = AssignmentHistory.HistoryStatus.valueOf(filterValue.toUpperCase());
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
                    } catch (IllegalArgumentException e) {
                        return Page.empty(pageable);
                    }
                    break;
                 case "acquisitionDate":
                    try {
                         // Assuming filterValue is in a format like YYYY-MM-DD
                        LocalDateTime filterDateTime = LocalDateTime.parse(filterValue + "T00:00:00"); // Start of the day
                        spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("acquisitionDate"), filterDateTime));
                    } catch (DateTimeParseException e) {
                         return Page.empty(pageable);
                    }
                    break;
                 case "returnedDate":
                    try {
                         // Assuming filterValue is in a format like YYYY-MM-DD
                        LocalDateTime filterDateTime = LocalDateTime.parse(filterValue + "T00:00:00"); // Start of the day
                        spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("returnedDate"), filterDateTime));
                    } catch (DateTimeParseException e) {
                         return Page.empty(pageable);
                    }
                    break;
                // Add more filter cases as needed
            }
        }

        return assignmentHistoryRepository.findAll(spec, pageable);
    }
} 