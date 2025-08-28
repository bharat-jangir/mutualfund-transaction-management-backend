package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.SchemeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchemeMasterRepository extends JpaRepository<SchemeMaster, Integer> {
    Optional<SchemeMaster> findByIsinCode(String isinCode);
    Optional<SchemeMaster> findBySchemeCode(String schemeCode);
    List<SchemeMaster> findByAmcAmcId(Integer amcId);
    List<SchemeMaster> findBySchemeNameContainingIgnoreCase(String schemeName);
}
