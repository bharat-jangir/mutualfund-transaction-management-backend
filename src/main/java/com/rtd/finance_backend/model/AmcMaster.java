package com.rtd.finance_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "amc_master_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmcMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amc_id")
    private Integer amcId;

    @Column(name = "amc_code", nullable = false, length = 5, unique = true)
    @NotBlank(message = "AMC code is required")
    @Size(min = 2, max = 5, message = "AMC code must be between 2 and 5 characters")
    private String amcCode;

    @Column(name = "amc_name", nullable = false, length = 100)
    @NotBlank(message = "AMC name is required")
    @Size(min = 2, max = 100, message = "AMC name must be between 2 and 100 characters")
    private String amcName;

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
