package com.rtd.finance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FifoCalculationDto {
    private String pan;
    private String folioNo;
    private Integer schemeId;
    private LocalDate redemptionDate;
    private BigDecimal redemptionUnits;
    private BigDecimal redemptionNav;
    private BigDecimal redemptionAmount;
    private BigDecimal totalCostBasis;
    private BigDecimal capitalGain;
    private BigDecimal capitalGainPercentage;
    private List<LotBreakdown> lotBreakdowns;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LotBreakdown {
        private Integer lotId;
        private LocalDate purchaseDate;
        private BigDecimal purchaseNav;
        private BigDecimal unitsUsed;
        private BigDecimal costBasis;
        private BigDecimal gainLoss;
    }
}
