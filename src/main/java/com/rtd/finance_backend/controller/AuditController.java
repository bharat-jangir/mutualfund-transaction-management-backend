package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.model.AuditLog;
import com.rtd.finance_backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogs = auditService.getAuditLogsPageable(pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", auditLogs));
    }

    @GetMapping("/logs/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogsByUsername(@PathVariable String username) {
        List<AuditLog> auditLogs = auditService.getAuditLogsByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", auditLogs));
    }

    @GetMapping("/logs/username/{username}/pageable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByUsernamePageable(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogs = auditService.getAuditLogsByUsernamePageable(username, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", auditLogs));
    }

    @GetMapping("/logs/action/{action}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogsByAction(@PathVariable String action) {
        List<AuditLog> auditLogs = auditService.getAuditLogsByAction(action);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", auditLogs));
    }

    @GetMapping("/logs/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLog> auditLogs = auditService.getAuditLogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", auditLogs));
    }

    @GetMapping("/logs/username/{username}/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogsByUsernameAndDateRange(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLog> auditLogs = auditService.getAuditLogsByUsernameAndDateRange(username, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", auditLogs));
    }
}
