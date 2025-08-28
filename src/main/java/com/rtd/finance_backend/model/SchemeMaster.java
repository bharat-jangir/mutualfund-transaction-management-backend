package com.rtd.finance_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "scheme_master_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheme_id")
    private Integer schemeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "amc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_amc_id"))
    @NotNull(message = "AMC is required")
    private AmcMaster amc;

    @Column(name = "scheme_code", nullable = false, length = 20)
    @NotBlank(message = "Scheme code is required")
    @Size(min = 3, max = 20, message = "Scheme code must be between 3 and 20 characters")
    private String schemeCode;

    @Column(name = "scheme_name", nullable = false, length = 150)
    @NotBlank(message = "Scheme name is required")
    @Size(min = 3, max = 150, message = "Scheme name must be between 3 and 150 characters")
    private String schemeName;

    @Column(name = "isin_code", nullable = false, length = 12, unique = true)
    @NotBlank(message = "ISIN code is required")
    @Size(min = 12, max = 12, message = "ISIN code must be exactly 12 characters")
    private String isinCode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
