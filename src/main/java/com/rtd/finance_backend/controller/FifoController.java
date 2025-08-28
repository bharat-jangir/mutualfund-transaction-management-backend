package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.dto.FifoCalculationDto;
import com.rtd.finance_backend.model.TransactionLot;
import com.rtd.finance_backend.service.FifoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fifo")
@CrossOrigin("*")
@Validated
public class FifoController {

    @Autowired
    private FifoService fifoService;

    @PostMapping("/calculate-redemption")
    public ResponseEntity<ApiResponse<FifoCalculationDto>> calculateFifoForRedemption(
            @RequestParam @NotBlank String pan,
            @RequestParam @NotNull Integer schemeId,
            @RequestParam @NotBlank String folioNo,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal redemptionUnits,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal redemptionNav,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate redemptionDate) {
        
        FifoCalculationDto result = fifoService.calculateFifoForRedemption(
            pan, schemeId, folioNo, redemptionUnits, redemptionNav, redemptionDate
        );
        return ResponseEntity.ok(ApiResponse.success("FIFO calculation completed", result));
    }

    @PostMapping("/process-redemption")
    public ResponseEntity<ApiResponse<FifoCalculationDto>> processRedemption(
            @RequestParam @NotBlank String pan,
            @RequestParam @NotNull Integer schemeId,
            @RequestParam @NotBlank String folioNo,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal redemptionUnits,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal redemptionNav,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate redemptionDate) {
        
        FifoCalculationDto result = fifoService.processRedemption(
            pan, schemeId, folioNo, redemptionUnits, redemptionNav, redemptionDate
        );
        return ResponseEntity.ok(ApiResponse.success("Redemption processed with FIFO", result));
    }

    @PostMapping("/create-purchase-lot")
    public ResponseEntity<ApiResponse<TransactionLot>> createPurchaseLot(
            @RequestParam @NotBlank String pan,
            @RequestParam @NotNull Integer schemeId,
            @RequestParam @NotBlank String folioNo,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal purchaseNav,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal purchaseUnits,
            @RequestParam @NotNull @DecimalMin("0.01") BigDecimal purchaseAmount) {
        
        TransactionLot lot = fifoService.createPurchaseLot(
            pan, schemeId, folioNo, purchaseDate, purchaseNav, purchaseUnits, purchaseAmount
        );
        return ResponseEntity.ok(ApiResponse.success("Purchase lot created", lot));
    }

    @GetMapping("/lots/{pan}/{schemeId}")
    public ResponseEntity<ApiResponse<List<TransactionLot>>> getCurrentLots(
            @PathVariable @NotBlank String pan,
            @PathVariable @NotNull Integer schemeId) {
        
        List<TransactionLot> lots = fifoService.getCurrentLots(pan, schemeId);
        return ResponseEntity.ok(ApiResponse.success("Current lots retrieved", lots));
    }

    @GetMapping("/remaining-units/{pan}/{schemeId}")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalRemainingUnits(
            @PathVariable @NotBlank String pan,
            @PathVariable @NotNull Integer schemeId) {
        
        BigDecimal units = fifoService.getTotalRemainingUnits(pan, schemeId);
        return ResponseEntity.ok(ApiResponse.success("Total remaining units", units));
    }

    @GetMapping("/cost-basis/{pan}/{schemeId}")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalCostBasis(
            @PathVariable @NotBlank String pan,
            @PathVariable @NotNull Integer schemeId) {
        
        BigDecimal costBasis = fifoService.getTotalCostBasis(pan, schemeId);
        return ResponseEntity.ok(ApiResponse.success("Total cost basis", costBasis));
    }

    @GetMapping("/unrealized-gain-loss/{pan}/{schemeId}")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateUnrealizedGainLoss(
            @PathVariable @NotBlank String pan,
            @PathVariable @NotNull Integer schemeId,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal currentNav) {
        
        BigDecimal gainLoss = fifoService.calculateUnrealizedGainLoss(pan, schemeId, currentNav);
        return ResponseEntity.ok(ApiResponse.success("Unrealized gain/loss calculated", gainLoss));
    }
}
