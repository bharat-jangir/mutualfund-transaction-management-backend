package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.dto.SwitchTransactionDto;
import com.rtd.finance_backend.dto.FifoCalculationDto;
import com.rtd.finance_backend.service.SwitchTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/switch")
@CrossOrigin("*")
@Validated
public class SwitchTransactionController {

    @Autowired
    private SwitchTransactionService switchTransactionService;

    @PostMapping("/switch-out")
    public ResponseEntity<ApiResponse<FifoCalculationDto>> processSwitchOut(
            @RequestParam @NotBlank String pan,
            @RequestParam @NotNull Integer sourceSchemeId,
            @RequestParam @NotBlank String folioNo,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal switchOutUnits,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal switchOutNav,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate switchDate) {
        
        FifoCalculationDto result = switchTransactionService.processSwitchOut(
            pan, sourceSchemeId, folioNo, switchOutUnits, switchOutNav, switchDate
        );
        return ResponseEntity.ok(ApiResponse.success("Switch-out processed successfully", result));
    }

    @PostMapping("/switch-in")
    public ResponseEntity<ApiResponse<String>> processSwitchIn(
            @RequestParam @NotBlank String pan,
            @RequestParam @NotNull Integer targetSchemeId,
            @RequestParam @NotBlank String folioNo,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal switchInUnits,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal switchInNav,
            @RequestParam @NotNull @DecimalMin("0.01") BigDecimal switchInAmount,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate switchDate) {
        
        switchTransactionService.processSwitchIn(
            pan, targetSchemeId, folioNo, switchInUnits, switchInNav, switchInAmount, switchDate
        );
        return ResponseEntity.ok(ApiResponse.success("Switch-in processed successfully", "Switch-in completed"));
    }

    @PostMapping("/complete-switch")
    public ResponseEntity<ApiResponse<SwitchTransactionDto>> processCompleteSwitch(
            @RequestParam @NotBlank String pan,
            @RequestParam @NotNull Integer sourceSchemeId,
            @RequestParam @NotNull Integer targetSchemeId,
            @RequestParam @NotBlank String folioNo,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal switchUnits,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal sourceNav,
            @RequestParam @NotNull @DecimalMin("0.0001") BigDecimal targetNav,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate switchDate) {
        
        SwitchTransactionDto result = switchTransactionService.processSwitchTransaction(
            pan, sourceSchemeId, targetSchemeId, folioNo, switchUnits, sourceNav, targetNav, switchDate
        );
        return ResponseEntity.ok(ApiResponse.success("Switch transaction processed successfully", result));
    }

    @GetMapping("/history/{pan}")
    public ResponseEntity<ApiResponse<List<SwitchTransactionDto>>> getSwitchHistory(
            @PathVariable @NotBlank String pan,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        
        List<SwitchTransactionDto> history = switchTransactionService.getSwitchHistory(pan, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("Switch history retrieved successfully", history));
    }
}
