package com.rtd.finance_backend.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "uploaded_transactions_info", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"pan", "txn_date", "scheme_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin("*")
public class UploadedTransactionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id")
    private Integer txnId;

    @Column(length = 10, nullable = false)
    private String pan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxStatus taxStatus;

    @Column(length = 50, nullable = false)
    private String txnType;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private LocalDate txnDate;

    @Column(precision = 18, scale = 4, nullable = false)
    private BigDecimal txnUnits;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal txnAmount;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal txnNav;

    @Column(nullable = false)
    private Integer schemeId;

    @Column(length = 30, nullable = false)
    private String folioNo;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public enum TaxStatus {
        INDIVIDUAL, PROPRITOR, HUF, COMPANY
    }

    // Getters and Setters
}
