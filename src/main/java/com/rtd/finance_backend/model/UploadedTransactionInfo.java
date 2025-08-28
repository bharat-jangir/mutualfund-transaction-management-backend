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
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "PAN is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "PAN must be in valid format")
    private String pan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Tax status is required")
    private TaxStatus taxStatus;

    @Column(length = 50, nullable = false)
    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "^(PURCHASE|REDEMPTION|SWITCH_IN|SWITCH_OUT)$", message = "Transaction type must be PURCHASE, REDEMPTION, SWITCH_IN, or SWITCH_OUT")
    private String txnType;

    @Column(nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(nullable = false)
    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate txnDate;

    @Column(precision = 18, scale = 4, nullable = false)
    @NotNull(message = "Transaction units are required")
    @DecimalMin(value = "0.0001", message = "Transaction units must be greater than 0")
    private BigDecimal txnUnits;

    @Column(precision = 18, scale = 2, nullable = false)
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    private BigDecimal txnAmount;

    @Column(precision = 10, scale = 4, nullable = false)
    @NotNull(message = "Transaction NAV is required")
    @DecimalMin(value = "0.0001", message = "Transaction NAV must be greater than 0")
    private BigDecimal txnNav;

    @Column(nullable = false)
    @NotNull(message = "Scheme ID is required")
    @Min(value = 1, message = "Scheme ID must be greater than 0")
    private Integer schemeId;

    @Column(length = 30, nullable = false)
    @NotBlank(message = "Folio number is required")
    @Size(min = 3, max = 30, message = "Folio number must be between 3 and 30 characters")
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
