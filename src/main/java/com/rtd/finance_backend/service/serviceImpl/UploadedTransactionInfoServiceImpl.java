package com.rtd.finance_backend.service.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rtd.finance_backend.model.UploadedTransactionInfo;
import com.rtd.finance_backend.repository.UploadedTransactionInfoRepository;
import com.rtd.finance_backend.service.UploadedTransactionInfoService;

@Service
public class UploadedTransactionInfoServiceImpl implements UploadedTransactionInfoService {

    @Autowired
    private UploadedTransactionInfoRepository repository;

    @Override
    public void saveAll(List<UploadedTransactionInfo> transactions) {
        repository.saveAll(transactions);
    }

    public void saveOrUpdate(List<UploadedTransactionInfo> excelTxns) {
        for (UploadedTransactionInfo txn : excelTxns) {
            Optional<UploadedTransactionInfo> existingTxn = repository
                    .findByPanAndTxnDateAndSchemeId(txn.getPan(), txn.getTxnDate(), txn.getSchemeId());

            if (existingTxn.isPresent()) {
                UploadedTransactionInfo existing = existingTxn.get();
                // update only editable fields
                existing.setTaxStatus(txn.getTaxStatus());
                existing.setTxnType(txn.getTxnType());
                existing.setDob(txn.getDob());
                existing.setTxnUnits(txn.getTxnUnits());
                existing.setTxnAmount(txn.getTxnAmount());  
                existing.setTxnNav(txn.getTxnNav());
                existing.setFolioNo(txn.getFolioNo());
                repository.save(existing);
            } else {
                repository.save(txn); // insert new
            }
        }
    }

    public void upsertTransactions(List<UploadedTransactionInfo> transactions) {
        for (UploadedTransactionInfo txn : transactions) {
            Optional<UploadedTransactionInfo> existingTxnOpt = repository.findByPanAndTxnDateAndSchemeId(
                    txn.getPan(), txn.getTxnDate(), txn.getSchemeId());

            if (existingTxnOpt.isPresent()) {
                UploadedTransactionInfo existingTxn = existingTxnOpt.get();

                // Update fields
                existingTxn.setTaxStatus(txn.getTaxStatus());
                existingTxn.setTxnType(txn.getTxnType());
                existingTxn.setDob(txn.getDob());
                existingTxn.setTxnUnits(txn.getTxnUnits());
                existingTxn.setTxnAmount(txn.getTxnAmount());
                existingTxn.setTxnNav(txn.getTxnNav());
                existingTxn.setFolioNo(txn.getFolioNo());
                existingTxn.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

                repository.save(existingTxn);
            } else {
                txn.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                repository.save(txn);
            }
        }
    }
}
