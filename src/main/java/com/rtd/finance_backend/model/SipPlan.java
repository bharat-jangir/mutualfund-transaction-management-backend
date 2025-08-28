package com.rtd.finance_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sip_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sipId;

    @Column(name = "pan", nullable = false, length = 10)
    @NotBlank(message = "PAN is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "PAN must be in valid format")
    private String pan;

    @Column(name = "scheme_id", nullable = false)
    @NotNull(message = "Scheme ID is required")
    @Min(value = 1, message = "Scheme ID must be greater than 0")
    private Integer schemeId;

    @Column(name = "folio_no", nullable = false, length = 30)
    @NotBlank(message = "Folio number is required")
    @Size(min = 3, max = 30, message = "Folio number must be between 3 and 30 characters")
    private String folioNo;

    @Column(name = "sip_amount", precision = 18, scale = 2, nullable = false)
    @NotNull(message = "SIP amount is required")
    @DecimalMin(value = "500.00", message = "SIP amount must be at least 500")
    private BigDecimal sipAmount;

    @Column(name = "frequency", nullable = false, length = 20)
    @NotBlank(message = "Frequency is required")
    @Pattern(regexp = "^(DAILY|WEEKLY|MONTHLY|QUARTERLY)$", message = "Frequency must be DAILY, WEEKLY, MONTHLY, or QUARTERLY")
    private String frequency;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "next_investment_date")
    private LocalDate nextInvestmentDate;

    @Column(name = "total_installments")
    private Integer totalInstallments;

    @Column(name = "completed_installments")
    private Integer completedInstallments = 0;

    @Column(name = "total_invested_amount", precision = 18, scale = 2)
    private BigDecimal totalInvestedAmount = BigDecimal.ZERO;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.nextInvestmentDate == null) {
            this.nextInvestmentDate = this.startDate;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        ACTIVE, PAUSED, CANCELLED, COMPLETED
    }
}
