package com.rtd.finance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioSummaryDto {
    private String pan;
    private String clientName;
    private String schemeName;
    private String amcName;
    private String folioNo;
    private BigDecimal totalUnits;
    private BigDecimal totalInvestment;
    private BigDecimal currentValue;
    private BigDecimal gainLoss;
    private BigDecimal gainLossPercentage;
    private LocalDateTime lastUpdated;
}
