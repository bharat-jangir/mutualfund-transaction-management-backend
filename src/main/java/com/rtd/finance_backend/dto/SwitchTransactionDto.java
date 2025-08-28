package com.rtd.finance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitchTransactionDto {
    private String pan;
    private String folioNo;
    private Integer sourceSchemeId;
    private String sourceSchemeName;
    private Integer targetSchemeId;
    private String targetSchemeName;
    private BigDecimal switchUnits;
    private BigDecimal sourceNav;
    private BigDecimal targetNav;
    private BigDecimal switchOutAmount;
    private BigDecimal switchInAmount;
    private BigDecimal capitalGain;
    private LocalDate switchDate;
    private String status; // "COMPLETED", "PENDING", "FAILED"
}
