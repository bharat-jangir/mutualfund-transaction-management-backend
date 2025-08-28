package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.dto.FifoCalculationDto;
import com.rtd.finance_backend.exception.ValidationException;
import com.rtd.finance_backend.model.TransactionLot;
import com.rtd.finance_backend.repository.TransactionLotRepository;
import com.rtd.finance_backend.service.FifoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FifoServiceImpl implements FifoService {

    @Autowired
    private TransactionLotRepository transactionLotRepository;

    @Override
    public FifoCalculationDto calculateFifoForRedemption(
            String pan, 
            Integer schemeId, 
            String folioNo, 
            BigDecimal redemptionUnits, 
            BigDecimal redemptionNav, 
            LocalDate redemptionDate) {
        
        // Get available lots ordered by purchase date (FIFO)
        List<TransactionLot> availableLots = transactionLotRepository
            .findActiveLotsByPanAndSchemeOrderByPurchaseDate(pan, schemeId);
        
        if (availableLots.isEmpty()) {
            throw new ValidationException("No purchase lots found for PAN: " + pan + " and Scheme: " + schemeId);
        }
        
        // Check if sufficient units are available
        BigDecimal totalAvailableUnits = availableLots.stream()
            .map(TransactionLot::getRemainingUnits)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (totalAvailableUnits.compareTo(redemptionUnits) < 0) {
            throw new ValidationException("Insufficient units available. Required: " + redemptionUnits + 
                ", Available: " + totalAvailableUnits);
        }
        
        // Calculate FIFO
        List<FifoCalculationDto.LotBreakdown> lotBreakdowns = new ArrayList<>();
        BigDecimal remainingRedemptionUnits = redemptionUnits;
        BigDecimal totalCostBasis = BigDecimal.ZERO;
        
        for (TransactionLot lot : availableLots) {
            if (remainingRedemptionUnits.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            BigDecimal unitsFromThisLot = lot.getRemainingUnits().min(remainingRedemptionUnits);
            BigDecimal costBasisForThisLot = unitsFromThisLot.multiply(lot.getPurchaseNav());
            BigDecimal saleProceedsForThisLot = unitsFromThisLot.multiply(redemptionNav);
            BigDecimal gainLossForThisLot = saleProceedsForThisLot.subtract(costBasisForThisLot);
            
            // Create lot breakdown
            FifoCalculationDto.LotBreakdown breakdown = new FifoCalculationDto.LotBreakdown(
                lot.getLotId(),
                lot.getPurchaseDate(),
                lot.getPurchaseNav(),
                unitsFromThisLot,
                costBasisForThisLot,
                gainLossForThisLot
            );
            lotBreakdowns.add(breakdown);
            
            totalCostBasis = totalCostBasis.add(costBasisForThisLot);
            remainingRedemptionUnits = remainingRedemptionUnits.subtract(unitsFromThisLot);
        }
        
        // Calculate total values
        BigDecimal redemptionAmount = redemptionUnits.multiply(redemptionNav);
        BigDecimal capitalGain = redemptionAmount.subtract(totalCostBasis);
        BigDecimal capitalGainPercentage = totalCostBasis.compareTo(BigDecimal.ZERO) > 0 
            ? capitalGain.divide(totalCostBasis, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
            : BigDecimal.ZERO;
        
        return new FifoCalculationDto(
            pan, folioNo, schemeId, redemptionDate, redemptionUnits, redemptionNav,
            redemptionAmount, totalCostBasis, capitalGain, capitalGainPercentage, lotBreakdowns
        );
    }

    @Override
    public TransactionLot createPurchaseLot(
            String pan, 
            Integer schemeId, 
            String folioNo, 
            LocalDate purchaseDate, 
            BigDecimal purchaseNav, 
            BigDecimal purchaseUnits, 
            BigDecimal purchaseAmount) {
        
        TransactionLot lot = new TransactionLot();
        lot.setPan(pan);
        lot.setSchemeId(schemeId);
        lot.setFolioNo(folioNo);
        lot.setPurchaseDate(purchaseDate);
        lot.setPurchaseNav(purchaseNav);
        lot.setPurchaseUnits(purchaseUnits);
        lot.setPurchaseAmount(purchaseAmount);
        lot.setRemainingUnits(purchaseUnits);
        lot.setIsActive(true);
        
        return transactionLotRepository.save(lot);
    }

    @Override
    public FifoCalculationDto processRedemption(
            String pan, 
            Integer schemeId, 
            String folioNo, 
            BigDecimal redemptionUnits, 
            BigDecimal redemptionNav, 
            LocalDate redemptionDate) {
        
        // Calculate FIFO first
        FifoCalculationDto fifoResult = calculateFifoForRedemption(
            pan, schemeId, folioNo, redemptionUnits, redemptionNav, redemptionDate
        );
        
        // Update the lots by reducing remaining units
        List<TransactionLot> availableLots = transactionLotRepository
            .findActiveLotsByPanAndSchemeOrderByPurchaseDate(pan, schemeId);
        
        BigDecimal remainingRedemptionUnits = redemptionUnits;
        
        for (TransactionLot lot : availableLots) {
            if (remainingRedemptionUnits.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            BigDecimal unitsToDeduct = lot.getRemainingUnits().min(remainingRedemptionUnits);
            lot.setRemainingUnits(lot.getRemainingUnits().subtract(unitsToDeduct));
            
            // Deactivate lot if no units remaining
            if (lot.getRemainingUnits().compareTo(BigDecimal.ZERO) <= 0) {
                lot.setIsActive(false);
            }
            
            transactionLotRepository.save(lot);
            remainingRedemptionUnits = remainingRedemptionUnits.subtract(unitsToDeduct);
        }
        
        return fifoResult;
    }

    @Override
    public List<TransactionLot> getCurrentLots(String pan, Integer schemeId) {
        return transactionLotRepository.findByPanAndSchemeIdAndIsActiveTrueOrderByPurchaseDateAsc(pan, schemeId);
    }

    @Override
    public BigDecimal getTotalRemainingUnits(String pan, Integer schemeId) {
        return transactionLotRepository.getTotalRemainingUnits(pan, schemeId);
    }

    @Override
    public BigDecimal getTotalCostBasis(String pan, Integer schemeId) {
        return transactionLotRepository.getTotalCostBasis(pan, schemeId);
    }

    @Override
    public BigDecimal calculateUnrealizedGainLoss(String pan, Integer schemeId, BigDecimal currentNav) {
        BigDecimal totalRemainingUnits = getTotalRemainingUnits(pan, schemeId);
        BigDecimal totalCostBasis = getTotalCostBasis(pan, schemeId);
        BigDecimal currentValue = totalRemainingUnits.multiply(currentNav);
        
        return currentValue.subtract(totalCostBasis);
    }
}
