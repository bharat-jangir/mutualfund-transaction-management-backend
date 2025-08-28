package com.rtd.finance_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rtd.finance_backend.helperclasses.ExcelHelper;
import com.rtd.finance_backend.model.UploadedTransactionInfo;
import com.rtd.finance_backend.service.UploadedTransactionInfoService;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin("*")
public class UploadedTransactionInfoController {

    @Autowired
    private UploadedTransactionInfoService uploadedTransactionInfoService;

    // @PostMapping("/upload")
    // public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
    //     if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
    //         return ResponseEntity.badRequest().body("Please upload an Excel file (.xlsx)");
    //     }

    //     try {
    //         List<UploadedTransactionInfo> transactions = ExcelHelper.parseExcel(file.getInputStream());
    //         service.saveAll(transactions);
    //         return ResponseEntity.ok("Uploaded and saved successfully!");
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    //     }
    // }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<UploadedTransactionInfo> transactions = ExcelHelper.parseExcel(file.getInputStream());
            uploadedTransactionInfoService.upsertTransactions(transactions);
            return ResponseEntity.ok("Upload and upsert successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

