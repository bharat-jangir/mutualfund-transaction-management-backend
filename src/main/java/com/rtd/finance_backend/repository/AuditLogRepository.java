package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByUsername(String username);
    
    List<AuditLog> findByAction(String action);
    
    List<AuditLog> findByEntityType(String entityType);
    
    List<AuditLog> findByStatus(String status);
    
    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM AuditLog a WHERE a.username = :username AND a.createdAt BETWEEN :startDate AND :endDate")
    List<AuditLog> findByUsernameAndDateRange(@Param("username") String username, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    Page<AuditLog> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
    
    Page<AuditLog> findByActionOrderByCreatedAtDesc(String action, Pageable pageable);
    
    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
