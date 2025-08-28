package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.SipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SipPlanRepository extends JpaRepository<SipPlan, Long> {
    
    List<SipPlan> findByPan(String pan);
    
    List<SipPlan> findByPanAndStatus(String pan, SipPlan.Status status);
    
    List<SipPlan> findBySchemeId(Integer schemeId);
    
    @Query("SELECT s FROM SipPlan s WHERE s.nextInvestmentDate <= :date AND s.status = 'ACTIVE'")
    List<SipPlan> findActiveSipsDueForInvestment(@Param("date") LocalDate date);
    
    @Query("SELECT s FROM SipPlan s WHERE s.pan = :pan AND s.schemeId = :schemeId AND s.status = 'ACTIVE'")
    Optional<SipPlan> findActiveSipByPanAndScheme(@Param("pan") String pan, @Param("schemeId") Integer schemeId);
    
    @Query("SELECT COUNT(s) FROM SipPlan s WHERE s.pan = :pan AND s.status = 'ACTIVE'")
    Long countActiveSipsByPan(@Param("pan") String pan);
    
    @Query("SELECT SUM(s.sipAmount) FROM SipPlan s WHERE s.pan = :pan AND s.status = 'ACTIVE'")
    java.math.BigDecimal getTotalActiveSipAmountByPan(@Param("pan") String pan);
}
