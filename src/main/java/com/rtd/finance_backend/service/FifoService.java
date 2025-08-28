package com.rtd.finance_backend.service;

import com.rtd.finance_backend.dto.FifoCalculationDto;
import com.rtd.finance_backend.model.TransactionLot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FifoService {
    
    /**
     * Calculate FIFO for a redemption transaction
     */
    FifoCalculationDto calculateFifoForRedemption(
        String pan, 
        Integer schemeId, 
        String folioNo, 
        BigDecimal redemptionUnits, 
        BigDecimal redemptionNav, 
        LocalDate redemptionDate
    );
    
    /**
     * Create a new purchase lot
     */
    TransactionLot createPurchaseLot(
        String pan, 
        Integer schemeId, 
        String folioNo, 
        LocalDate purchaseDate, 
        BigDecimal purchaseNav, 
        BigDecimal purchaseUnits, 
        BigDecimal purchaseAmount
    );
    
    /**
     * Process a redemption and update lots
     */
    FifoCalculationDto processRedemption(
        String pan, 
        Integer schemeId, 
        String folioNo, 
        BigDecimal redemptionUnits, 
        BigDecimal redemptionNav, 
        LocalDate redemptionDate
    );
    
    /**
     * Get current portfolio summary with FIFO details
     */
    List<TransactionLot> getCurrentLots(String pan, Integer schemeId);
    
    /**
     * Get total remaining units for a PAN and scheme
     */
    BigDecimal getTotalRemainingUnits(String pan, Integer schemeId);
    
    /**
     * Get total cost basis for remaining units
     */
    BigDecimal getTotalCostBasis(String pan, Integer schemeId);
    
    /**
     * Calculate unrealized gains/losses
     */
    BigDecimal calculateUnrealizedGainLoss(String pan, Integer schemeId, BigDecimal currentNav);
}
