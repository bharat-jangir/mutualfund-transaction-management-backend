package com.rtd.finance_backend.service;

import com.rtd.finance_backend.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {
    
    void logActivity(String username, String action, String entityType, String entityId, String details, String status);
    
    void logError(String username, String action, String entityType, String entityId, String errorMessage);
    
    void logUserActivity(String username, String action, String details);
    
    void logTransactionActivity(String username, String action, String entityId, String details);
    
    List<AuditLog> getAuditLogsByUsername(String username);
    
    List<AuditLog> getAuditLogsByAction(String action);
    
    List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<AuditLog> getAuditLogsByUsernameAndDateRange(String username, LocalDateTime startDate, LocalDateTime endDate);
    
    Page<AuditLog> getAuditLogsPageable(Pageable pageable);
    
    Page<AuditLog> getAuditLogsByUsernamePageable(String username, Pageable pageable);
}
