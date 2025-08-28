package com.rtd.finance_backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rtd.finance_backend.model.UploadedTransactionInfo;

@Repository
public interface UploadedTransactionInfoRepository extends JpaRepository<UploadedTransactionInfo, Long> {
    Optional<UploadedTransactionInfo> findByPanAndTxnDateAndSchemeId(String pan, LocalDate txnDate, Integer schemeId);
}
