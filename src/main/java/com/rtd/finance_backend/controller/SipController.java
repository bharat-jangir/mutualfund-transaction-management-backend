package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.model.SipPlan;
import com.rtd.finance_backend.service.SipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sip")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SipController {

    private final SipService sipService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SipPlan>> createSipPlan(@Valid @RequestBody SipPlan sipPlan) {
        SipPlan createdSip = sipService.createSipPlan(sipPlan);
        return ResponseEntity.ok(ApiResponse.success("SIP plan created successfully", createdSip));
    }

    @PutMapping("/{sipId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SipPlan>> updateSipPlan(
            @PathVariable Long sipId, 
            @Valid @RequestBody SipPlan sipPlan) {
        SipPlan updatedSip = sipService.updateSipPlan(sipId, sipPlan);
        return ResponseEntity.ok(ApiResponse.success("SIP plan updated successfully", updatedSip));
    }

    @DeleteMapping("/{sipId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Void>> cancelSipPlan(@PathVariable Long sipId) {
        sipService.cancelSipPlan(sipId);
        return ResponseEntity.ok(ApiResponse.success("SIP plan cancelled successfully", null));
    }

    @PostMapping("/{sipId}/pause")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Void>> pauseSipPlan(@PathVariable Long sipId) {
        sipService.pauseSipPlan(sipId);
        return ResponseEntity.ok(ApiResponse.success("SIP plan paused successfully", null));
    }

    @PostMapping("/{sipId}/resume")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Void>> resumeSipPlan(@PathVariable Long sipId) {
        sipService.resumeSipPlan(sipId);
        return ResponseEntity.ok(ApiResponse.success("SIP plan resumed successfully", null));
    }

    @GetMapping("/{sipId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<SipPlan>> getSipPlanById(@PathVariable Long sipId) {
        SipPlan sipPlan = sipService.getSipPlanById(sipId);
        return ResponseEntity.ok(ApiResponse.success(sipPlan));
    }

    @GetMapping("/pan/{pan}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<SipPlan>>> getSipPlansByPan(@PathVariable String pan) {
        List<SipPlan> sipPlans = sipService.getSipPlansByPan(pan);
        return ResponseEntity.ok(ApiResponse.success("SIP plans retrieved successfully", sipPlans));
    }

    @GetMapping("/pan/{pan}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<SipPlan>>> getActiveSipPlansByPan(@PathVariable String pan) {
        List<SipPlan> activeSips = sipService.getActiveSipPlansByPan(pan);
        return ResponseEntity.ok(ApiResponse.success("Active SIP plans retrieved successfully", activeSips));
    }

    @GetMapping("/scheme/{schemeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<SipPlan>>> getSipPlansByScheme(@PathVariable Integer schemeId) {
        List<SipPlan> sipPlans = sipService.getSipPlansByScheme(schemeId);
        return ResponseEntity.ok(ApiResponse.success("SIP plans retrieved successfully", sipPlans));
    }

    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> processSipInvestments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        sipService.processSipInvestments(date);
        return ResponseEntity.ok(ApiResponse.success("SIP investments processed successfully", null));
    }

    @GetMapping("/summary/{pan}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSipSummary(@PathVariable String pan) {
        Map<String, Object> summary = sipService.getSipSummary(pan);
        return ResponseEntity.ok(ApiResponse.success("SIP summary retrieved successfully", summary));
    }

    @GetMapping("/amount/{pan}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalActiveSipAmount(@PathVariable String pan) {
        BigDecimal amount = sipService.getTotalActiveSipAmount(pan);
        return ResponseEntity.ok(ApiResponse.success("Total active SIP amount retrieved successfully", amount));
    }

    @GetMapping("/count/{pan}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER', 'VIEWER')")
    public ResponseEntity<ApiResponse<Long>> getActiveSipCount(@PathVariable String pan) {
        Long count = sipService.getActiveSipCount(pan);
        return ResponseEntity.ok(ApiResponse.success("Active SIP count retrieved successfully", count));
    }
}
