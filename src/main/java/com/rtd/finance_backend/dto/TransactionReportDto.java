package com.rtd.finance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportDto {
    private String pan;
    private String clientName;
    private String schemeName;
    private String amcName;
    private String txnType;
    private LocalDate txnDate;
    private BigDecimal txnUnits;
    private BigDecimal txnAmount;
    private BigDecimal txnNav;
    private String folioNo;
    private String taxStatus;
}
