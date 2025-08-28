package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.dto.SwitchTransactionDto;
import com.rtd.finance_backend.dto.FifoCalculationDto;
import com.rtd.finance_backend.exception.ValidationException;
import com.rtd.finance_backend.model.SchemeMaster;
import com.rtd.finance_backend.repository.SchemeMasterRepository;
import com.rtd.finance_backend.service.SwitchTransactionService;
import com.rtd.finance_backend.service.FifoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SwitchTransactionServiceImpl implements SwitchTransactionService {

    @Autowired
    private FifoService fifoService;
    
    @Autowired
    private SchemeMasterRepository schemeRepository;

    @Override
    public FifoCalculationDto processSwitchOut(
            String pan, 
            Integer sourceSchemeId, 
            String folioNo, 
            BigDecimal switchOutUnits, 
            BigDecimal switchOutNav, 
            LocalDate switchDate) {
        
        // Validate source scheme exists
        SchemeMaster sourceScheme = schemeRepository.findById(sourceSchemeId)
            .orElseThrow(() -> new ValidationException("Source scheme not found with ID: " + sourceSchemeId));
        
        // Process switch-out using FIFO (same as redemption)
        return fifoService.processRedemption(pan, sourceSchemeId, folioNo, switchOutUnits, switchOutNav, switchDate);
    }

    @Override
    public void processSwitchIn(
            String pan, 
            Integer targetSchemeId, 
            String folioNo, 
            BigDecimal switchInUnits, 
            BigDecimal switchInNav, 
            BigDecimal switchInAmount, 
            LocalDate switchDate) {
        
        // Validate target scheme exists
        SchemeMaster targetScheme = schemeRepository.findById(targetSchemeId)
            .orElseThrow(() -> new ValidationException("Target scheme not found with ID: " + targetSchemeId));
        
        // Create purchase lot for switch-in (same as purchase)
        fifoService.createPurchaseLot(pan, targetSchemeId, folioNo, switchDate, switchInNav, switchInUnits, switchInAmount);
    }

    @Override
    public SwitchTransactionDto processSwitchTransaction(
            String pan, 
            Integer sourceSchemeId, 
            Integer targetSchemeId, 
            String folioNo, 
            BigDecimal switchUnits, 
            BigDecimal sourceNav, 
            BigDecimal targetNav, 
            LocalDate switchDate) {
        
        // Validate schemes exist
        SchemeMaster sourceScheme = schemeRepository.findById(sourceSchemeId)
            .orElseThrow(() -> new ValidationException("Source scheme not found with ID: " + sourceSchemeId));
        
        SchemeMaster targetScheme = schemeRepository.findById(targetSchemeId)
            .orElseThrow(() -> new ValidationException("Target scheme not found with ID: " + targetSchemeId));
        
        // Calculate amounts
        BigDecimal switchOutAmount = switchUnits.multiply(sourceNav);
        BigDecimal switchInAmount = switchUnits.multiply(targetNav);
        BigDecimal capitalGain = switchOutAmount.subtract(switchInAmount);
        
        try {
            // Step 1: Process switch-out
            FifoCalculationDto switchOutResult = processSwitchOut(pan, sourceSchemeId, folioNo, switchUnits, sourceNav, switchDate);
            
            // Step 2: Process switch-in
            processSwitchIn(pan, targetSchemeId, folioNo, switchUnits, targetNav, switchInAmount, switchDate);
            
            // Create switch transaction DTO
            return new SwitchTransactionDto(
                pan, folioNo, sourceSchemeId, sourceScheme.getSchemeName(),
                targetSchemeId, targetScheme.getSchemeName(), switchUnits,
                sourceNav, targetNav, switchOutAmount, switchInAmount,
                capitalGain, switchDate, "COMPLETED"
            );
            
        } catch (Exception e) {
            // Return failed transaction
            return new SwitchTransactionDto(
                pan, folioNo, sourceSchemeId, sourceScheme.getSchemeName(),
                targetSchemeId, targetScheme.getSchemeName(), switchUnits,
                sourceNav, targetNav, switchOutAmount, switchInAmount,
                capitalGain, switchDate, "FAILED"
            );
        }
    }

    @Override
    public List<SwitchTransactionDto> getSwitchHistory(String pan, LocalDate fromDate, LocalDate toDate) {
        // This would typically query a switch transaction table
        // For now, return empty list as we don't have a dedicated switch transaction table
        // In a real implementation, you would:
        // 1. Create a SwitchTransaction entity
        // 2. Store switch transactions in the database
        // 3. Query the switch transaction table
        
        return new ArrayList<>();
    }
}
