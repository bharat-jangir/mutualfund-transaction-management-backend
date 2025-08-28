package com.rtd.finance_backend.service;

import com.rtd.finance_backend.model.SipPlan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SipService {
    
    SipPlan createSipPlan(SipPlan sipPlan);
    
    SipPlan updateSipPlan(Long sipId, SipPlan sipPlan);
    
    void cancelSipPlan(Long sipId);
    
    void pauseSipPlan(Long sipId);
    
    void resumeSipPlan(Long sipId);
    
    SipPlan getSipPlanById(Long sipId);
    
    List<SipPlan> getSipPlansByPan(String pan);
    
    List<SipPlan> getActiveSipPlansByPan(String pan);
    
    List<SipPlan> getSipPlansByScheme(Integer schemeId);
    
    void processSipInvestments(LocalDate date);
    
    Map<String, Object> getSipSummary(String pan);
    
    BigDecimal getTotalActiveSipAmount(String pan);
    
    Long getActiveSipCount(String pan);
}
