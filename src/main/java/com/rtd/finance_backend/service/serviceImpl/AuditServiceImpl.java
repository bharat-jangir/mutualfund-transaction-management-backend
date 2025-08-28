package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.model.AuditLog;
import com.rtd.finance_backend.repository.AuditLogRepository;
import com.rtd.finance_backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void logActivity(String username, String action, String entityType, String entityId, String details, String status) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .username(username)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .details(details)
                    .status(status)
                    .build();
            
            auditLogRepository.save(auditLog);
            log.debug("Audit log created: {} - {} - {}", username, action, entityType);
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage(), e);
        }
    }

    @Override
    public void logError(String username, String action, String entityType, String entityId, String errorMessage) {
        logActivity(username, action, entityType, entityId, "Error occurred", "ERROR");
    }

    @Override
    public void logUserActivity(String username, String action, String details) {
        logActivity(username, action, "USER", null, details, "SUCCESS");
    }

    @Override
    public void logTransactionActivity(String username, String action, String entityId, String details) {
        logActivity(username, action, "TRANSACTION", entityId, details, "SUCCESS");
    }

    @Override
    public List<AuditLog> getAuditLogsByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    @Override
    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    @Override
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<AuditLog> getAuditLogsByUsernameAndDateRange(String username, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByUsernameAndDateRange(username, startDate, endDate);
    }

    @Override
    public Page<AuditLog> getAuditLogsPageable(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<AuditLog> getAuditLogsByUsernamePageable(String username, Pageable pageable) {
        return auditLogRepository.findByUsernameOrderByCreatedAtDesc(username, pageable);
    }
}
