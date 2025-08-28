package com.rtd.finance_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_lots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_id")
    private Integer lotId;

    @Column(name = "pan", nullable = false, length = 10)
    private String pan;

    @Column(name = "scheme_id", nullable = false)
    private Integer schemeId;

    @Column(name = "folio_no", nullable = false, length = 30)
    private String folioNo;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "purchase_nav", precision = 10, scale = 4, nullable = false)
    private BigDecimal purchaseNav;

    @Column(name = "purchase_units", precision = 18, scale = 4, nullable = false)
    private BigDecimal purchaseUnits;

    @Column(name = "purchase_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal purchaseAmount;

    @Column(name = "remaining_units", precision = 18, scale = 4, nullable = false)
    private BigDecimal remainingUnits;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.remainingUnits = this.purchaseUnits; // Initially, all units are remaining
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
