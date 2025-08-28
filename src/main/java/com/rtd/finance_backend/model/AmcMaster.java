package com.rtd.finance_backend.model;

import jakarta.persistence.*;
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
    private String amcCode;

    @Column(name = "amc_name", nullable = false, length = 100)
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
