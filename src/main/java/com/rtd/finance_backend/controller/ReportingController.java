package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.dto.PortfolioSummaryDto;
import com.rtd.finance_backend.dto.TransactionReportDto;
import com.rtd.finance_backend.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
@Validated
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    // Portfolio Reports
    @GetMapping("/portfolio/{pan}")
    public ResponseEntity<ApiResponse<PortfolioSummaryDto>> getPortfolioSummary(
            @PathVariable @NotBlank String pan) {
        PortfolioSummaryDto portfolio = reportingService.getPortfolioSummary(pan);
        return ResponseEntity.ok(ApiResponse.success("Portfolio summary retrieved", portfolio));
    }

    @GetMapping("/portfolios")
    public ResponseEntity<ApiResponse<List<PortfolioSummaryDto>>> getAllPortfolios() {
        List<PortfolioSummaryDto> portfolios = reportingService.getAllPortfolios();
        return ResponseEntity.ok(ApiResponse.success("All portfolios retrieved", portfolios));
    }

    @GetMapping("/portfolio-value/{pan}")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getPortfolioValueByScheme(
            @PathVariable @NotBlank String pan) {
        Map<String, BigDecimal> portfolioValues = reportingService.getPortfolioValueByScheme(pan);
        return ResponseEntity.ok(ApiResponse.success("Portfolio values by scheme retrieved", portfolioValues));
    }

    // Transaction Reports
    @GetMapping("/transactions/{pan}")
    public ResponseEntity<ApiResponse<List<TransactionReportDto>>> getTransactionHistory(
            @PathVariable @NotBlank String pan,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<TransactionReportDto> transactions = reportingService.getTransactionHistory(pan, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved", transactions));
    }

    @GetMapping("/transactions/scheme/{schemeId}")
    public ResponseEntity<ApiResponse<List<TransactionReportDto>>> getTransactionHistoryByScheme(
            @PathVariable @NotNull Integer schemeId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<TransactionReportDto> transactions = reportingService.getTransactionHistoryByScheme(schemeId, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("Transaction history by scheme retrieved", transactions));
    }

    @GetMapping("/transaction-summary/{pan}")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getTransactionSummaryByType(
            @PathVariable @NotBlank String pan,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        Map<String, BigDecimal> summary = reportingService.getTransactionSummaryByType(pan, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("Transaction summary by type retrieved", summary));
    }

    // AMC Reports
    @GetMapping("/amc-investment-summary")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getAmcWiseInvestmentSummary() {
        Map<String, BigDecimal> amcSummary = reportingService.getAmcWiseInvestmentSummary();
        return ResponseEntity.ok(ApiResponse.success("AMC-wise investment summary retrieved", amcSummary));
    }

    @GetMapping("/top-schemes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopSchemesByInvestment() {
        List<Map<String, Object>> topSchemes = reportingService.getTopSchemesByInvestment();
        return ResponseEntity.ok(ApiResponse.success("Top schemes by investment retrieved", topSchemes));
    }

    // Client Reports
    @GetMapping("/client-count-by-tax-status")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getClientCountByTaxStatus() {
        Map<String, Long> clientCount = reportingService.getClientCountByTaxStatus();
        return ResponseEntity.ok(ApiResponse.success("Client count by tax status retrieved", clientCount));
    }

    @GetMapping("/top-clients")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopClientsByInvestment() {
        List<Map<String, Object>> topClients = reportingService.getTopClientsByInvestment();
        return ResponseEntity.ok(ApiResponse.success("Top clients by investment retrieved", topClients));
    }

    // NAV Reports
    @GetMapping("/current-nav")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getCurrentNavByScheme() {
        Map<String, BigDecimal> currentNavs = reportingService.getCurrentNavByScheme();
        return ResponseEntity.ok(ApiResponse.success("Current NAV by scheme retrieved", currentNavs));
    }

    @GetMapping("/nav-history/{schemeId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNavHistory(
            @PathVariable @NotNull Integer schemeId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<Map<String, Object>> navHistory = reportingService.getNavHistory(schemeId, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("NAV history retrieved", navHistory));
    }
}
