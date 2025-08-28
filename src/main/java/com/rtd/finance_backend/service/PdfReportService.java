package com.rtd.finance_backend.service;

import java.time.LocalDate;

public interface PdfReportService {
    
    byte[] generatePortfolioReport(String pan, LocalDate asOfDate);
    
    byte[] generateTransactionReport(String pan, LocalDate fromDate, LocalDate toDate);
    
    byte[] generateSipReport(String pan);
    
    byte[] generateCapitalGainsReport(String pan, LocalDate fromDate, LocalDate toDate);
    
    byte[] generateClientStatement(String pan, LocalDate fromDate, LocalDate toDate);
}
