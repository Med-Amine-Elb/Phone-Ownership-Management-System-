package com.phone.service;

import com.phone.model.AuditLog;
import com.phone.repository.AuditLogRepository;
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
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Page<AuditLog> getAuditLogs(Pageable pageable, String filterField, String filterValue) {
        Specification<AuditLog> spec = Specification.where(null);

        if (filterField != null && filterValue != null && !filterValue.isEmpty()) {
            switch (filterField) {
                case "action":
                    spec = spec.and((root, query, cb) -> cb.equal(root.get("action"), AuditLog.ActionType.valueOf(filterValue.toUpperCase())));
                    break;
                case "entityType":
                    spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("entityType")), "%" + filterValue.toLowerCase() + "%"));
                    break;
                case "entityId":
                    try {
                        Long entityId = Long.parseLong(filterValue);
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("entityId"), entityId));
                    } catch (NumberFormatException e) {
                        // Invalid ID, return empty page
                        return Page.empty(pageable);
                    }
                    break;
                case "performedBy":
                     // Assuming filterValue is the username of the system user
                    spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("performedBy").get("username")), "%" + filterValue.toLowerCase() + "%"));
                    break;
                case "timestamp":
                    try {
                         // Assuming filterValue is in a format like YYYY-MM-DD
                        LocalDateTime filterDateTime = LocalDateTime.parse(filterValue + "T00:00:00"); // Start of the day
                        spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), filterDateTime));
                    } catch (DateTimeParseException e) {
                        // Invalid date format, return empty page
                         return Page.empty(pageable);
                    }
                    break;
                // Add more filter cases as needed
            }
        }

        // Note: Audit logs are not soft-deleted, so we don't need a deletedFalse check here.
        return auditLogRepository.findAll(spec, pageable);
    }
} 