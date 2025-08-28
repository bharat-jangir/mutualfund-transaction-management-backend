package com.rtd.finance_backend.service;

import com.rtd.finance_backend.dto.PortfolioSummaryDto;
import com.rtd.finance_backend.dto.TransactionReportDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportingService {
    
    // Portfolio Reports
    PortfolioSummaryDto getPortfolioSummary(String pan);
    List<PortfolioSummaryDto> getAllPortfolios();
    Map<String, BigDecimal> getPortfolioValueByScheme(String pan);
    
    // Transaction Reports
    List<TransactionReportDto> getTransactionHistory(String pan, LocalDate fromDate, LocalDate toDate);
    List<TransactionReportDto> getTransactionHistoryByScheme(Integer schemeId, LocalDate fromDate, LocalDate toDate);
    Map<String, BigDecimal> getTransactionSummaryByType(String pan, LocalDate fromDate, LocalDate toDate);
    
    // AMC Reports
    Map<String, BigDecimal> getAmcWiseInvestmentSummary();
    List<Map<String, Object>> getTopSchemesByInvestment();
    
    // Client Reports
    Map<String, Long> getClientCountByTaxStatus();
    List<Map<String, Object>> getTopClientsByInvestment();
    
    // NAV Reports
    Map<String, BigDecimal> getCurrentNavByScheme();
    List<Map<String, Object>> getNavHistory(Integer schemeId, LocalDate fromDate, LocalDate toDate);
}
