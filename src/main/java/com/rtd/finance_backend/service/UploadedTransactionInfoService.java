package com.rtd.finance_backend.service;

import java.util.List;

import com.rtd.finance_backend.model.UploadedTransactionInfo;

public interface UploadedTransactionInfoService {
     public void saveAll(List<UploadedTransactionInfo> transactions);
     public void saveOrUpdate(List<UploadedTransactionInfo> excelTxns);
     public void upsertTransactions(List<UploadedTransactionInfo> transactions);
}
