package com.phone.controller;

import com.phone.model.AssignmentHistory;
import com.phone.service.AssignmentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignment-history")
@CrossOrigin(origins = "http://localhost:5173")
public class AssignmentHistoryController {

    @Autowired
    private AssignmentHistoryService assignmentHistoryService;

    @GetMapping
    public ResponseEntity<Page<com.phone.dto.AssignmentHistoryDto>> getAllAssignmentHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "acquisitionDate,desc") String[] sort,
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<AssignmentHistory> assignmentHistoryPage = assignmentHistoryService.getAssignmentHistory(
                pageable, filterField, filterValue);

        return ResponseEntity.ok(assignmentHistoryPage.map(AssignmentHistory::toDto));
    }
} 