package com.phone.controller;

import com.phone.model.AuditLog;
import com.phone.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "http://localhost:5173")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<Page<com.phone.dto.AuditLogDto>> getAllAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp,desc") String[] sort,
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<AuditLog> auditLogsPage = auditLogService.getAuditLogs(
                pageable, filterField, filterValue);

        return ResponseEntity.ok(auditLogsPage.map(AuditLog::toDto));
    }
} 