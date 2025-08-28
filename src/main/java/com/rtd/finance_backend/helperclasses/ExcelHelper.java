package com.rtd.finance_backend.helperclasses;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rtd.finance_backend.model.UploadedTransactionInfo;
import com.rtd.finance_backend.model.TransactionLot;

public class ExcelHelper {

    public static List<UploadedTransactionInfo> parseExcel(InputStream inputStream) {
        List<UploadedTransactionInfo> transactions = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber++ == 0) continue; // Skip header

                UploadedTransactionInfo txn = new UploadedTransactionInfo();
                txn.setPan(getCellValue(currentRow.getCell(1)));
                txn.setTaxStatus(UploadedTransactionInfo.TaxStatus.valueOf(getCellValue(currentRow.getCell(2)).toUpperCase()));  
                txn.setTxnType(getCellValue(currentRow.getCell(3)));
                txn.setDob(currentRow.getCell(4).getLocalDateTimeCellValue().toLocalDate());
                txn.setTxnDate(currentRow.getCell(5).getLocalDateTimeCellValue().toLocalDate());
                txn.setTxnUnits(BigDecimal.valueOf(currentRow.getCell(6).getNumericCellValue()));
                txn.setTxnAmount(BigDecimal.valueOf(currentRow.getCell(7).getNumericCellValue()));
                txn.setTxnNav(BigDecimal.valueOf(currentRow.getCell(8).getNumericCellValue()));
                txn.setSchemeId((int) currentRow.getCell(9).getNumericCellValue());
                txn.setFolioNo(getCellValue(currentRow.getCell(10)));

                transactions.add(txn);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        transactions.forEach(n->System.out.println(n.toString()));
        return transactions;
    }

    /**
     * Create TransactionLot for purchase transactions
     */
    public static TransactionLot createTransactionLotFromTransaction(UploadedTransactionInfo transaction) {
        if (!"PURCHASE".equalsIgnoreCase(transaction.getTxnType())) {
            throw new IllegalArgumentException("Transaction must be a PURCHASE to create a lot");
        }
        
        TransactionLot lot = new TransactionLot();
        lot.setPan(transaction.getPan());
        lot.setSchemeId(transaction.getSchemeId());
        lot.setFolioNo(transaction.getFolioNo());
        lot.setPurchaseDate(transaction.getTxnDate());
        lot.setPurchaseNav(transaction.getTxnNav());
        lot.setPurchaseUnits(transaction.getTxnUnits());
        lot.setPurchaseAmount(transaction.getTxnAmount());
        lot.setRemainingUnits(transaction.getTxnUnits());
        lot.setIsActive(true);
        
        return lot;
    }

    // Helper method to safely get cell values (either string or numeric)
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
