package com.rtd.finance_backend.model;

import jakarta.persistence.*;
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
    private AmcMaster amc;

    @Column(name = "scheme_code", nullable = false, length = 20)
    private String schemeCode;

    @Column(name = "scheme_name", nullable = false, length = 150)
    private String schemeName;

    @Column(name = "isin_code", nullable = false, length = 12, unique = true)
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
