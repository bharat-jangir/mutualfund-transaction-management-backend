package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.AmcMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmcMasterRepository extends JpaRepository<AmcMaster, Integer> {
    Optional<AmcMaster> findByAmcCode(String amcCode);
    Optional<AmcMaster> findByAmcName(String amcName);
}
