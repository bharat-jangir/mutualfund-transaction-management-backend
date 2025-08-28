package com.rtd.finance_backend.service;

import com.rtd.finance_backend.dto.SwitchTransactionDto;
import com.rtd.finance_backend.dto.FifoCalculationDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SwitchTransactionService {
    
    /**
     * Process a switch-out transaction (sell from source scheme)
     */
    FifoCalculationDto processSwitchOut(
        String pan, 
        Integer sourceSchemeId, 
        String folioNo, 
        BigDecimal switchOutUnits, 
        BigDecimal switchOutNav, 
        LocalDate switchDate
    );
    
    /**
     * Process a switch-in transaction (buy into target scheme)
     */
    void processSwitchIn(
        String pan, 
        Integer targetSchemeId, 
        String folioNo, 
        BigDecimal switchInUnits, 
        BigDecimal switchInNav, 
        BigDecimal switchInAmount, 
        LocalDate switchDate
    );
    
    /**
     * Process a complete switch transaction (switch-out + switch-in)
     */
    SwitchTransactionDto processSwitchTransaction(
        String pan, 
        Integer sourceSchemeId, 
        Integer targetSchemeId, 
        String folioNo, 
        BigDecimal switchUnits, 
        BigDecimal sourceNav, 
        BigDecimal targetNav, 
        LocalDate switchDate
    );
    
    /**
     * Get switch transaction history for a PAN
     */
    java.util.List<SwitchTransactionDto> getSwitchHistory(String pan, LocalDate fromDate, LocalDate toDate);
}
