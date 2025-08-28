package com.rtd.finance_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rtd.finance_backend.model.UploadedTransactionInfo;

@Repository
public interface UploadedTransactionInfoRepository extends JpaRepository<UploadedTransactionInfo, Long> {
    Optional<UploadedTransactionInfo> findByPanAndTxnDateAndSchemeId(String pan, LocalDate txnDate, Integer schemeId);
    
    // Additional methods for reporting
    List<UploadedTransactionInfo> findByPan(String pan);
    List<UploadedTransactionInfo> findBySchemeId(Integer schemeId);
    List<UploadedTransactionInfo> findByPanAndTxnDateBetween(String pan, LocalDate fromDate, LocalDate toDate);
    List<UploadedTransactionInfo> findBySchemeIdAndTxnDateBetween(Integer schemeId, LocalDate fromDate, LocalDate toDate);
}
