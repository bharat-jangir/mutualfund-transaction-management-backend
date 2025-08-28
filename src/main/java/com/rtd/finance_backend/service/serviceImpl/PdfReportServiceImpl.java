package com.rtd.finance_backend.service.serviceImpl;

import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;
import com.rtd.finance_backend.dto.PortfolioSummaryDto;
import com.rtd.finance_backend.dto.TransactionReportDto;
import com.rtd.finance_backend.model.SipPlan;
import com.rtd.finance_backend.service.PdfReportService;
import com.rtd.finance_backend.service.ReportingService;
import com.rtd.finance_backend.service.SipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfReportServiceImpl implements PdfReportService {

    private final ReportingService reportingService;
    private final SipService sipService;

    @Override
    public byte[] generatePortfolioReport(String pan, LocalDate asOfDate) {
        try {
            PortfolioSummaryDto portfolio = reportingService.getPortfolioSummary(pan);
            
            String html = generatePortfolioHtml(portfolio, asOfDate);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate portfolio report for PAN: {}", pan, e);
            throw new RuntimeException("Failed to generate portfolio report", e);
        }
    }

    @Override
    public byte[] generateTransactionReport(String pan, LocalDate fromDate, LocalDate toDate) {
        try {
            List<TransactionReportDto> transactions = reportingService.getTransactionHistory(pan, fromDate, toDate);
            
            String html = generateTransactionHtml(transactions, pan, fromDate, toDate);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate transaction report for PAN: {}", pan, e);
            throw new RuntimeException("Failed to generate transaction report", e);
        }
    }

    @Override
    public byte[] generateSipReport(String pan) {
        try {
            Map<String, Object> sipSummary = sipService.getSipSummary(pan);
            List<SipPlan> activeSips = sipService.getActiveSipPlansByPan(pan);
            
            String html = generateSipHtml(sipSummary, activeSips, pan);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate SIP report for PAN: {}", pan, e);
            throw new RuntimeException("Failed to generate SIP report", e);
        }
    }

    @Override
    public byte[] generateCapitalGainsReport(String pan, LocalDate fromDate, LocalDate toDate) {
        try {
            // This would need to be implemented based on FIFO calculations
            String html = generateCapitalGainsHtml(pan, fromDate, toDate);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate capital gains report for PAN: {}", pan, e);
            throw new RuntimeException("Failed to generate capital gains report", e);
        }
    }

    @Override
    public byte[] generateClientStatement(String pan, LocalDate fromDate, LocalDate toDate) {
        try {
            PortfolioSummaryDto portfolio = reportingService.getPortfolioSummary(pan);
            List<TransactionReportDto> transactions = reportingService.getTransactionHistory(pan, fromDate, toDate);
            
            String html = generateClientStatementHtml(portfolio, transactions, pan, fromDate, toDate);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate client statement for PAN: {}", pan, e);
            throw new RuntimeException("Failed to generate client statement", e);
        }
    }

    private String generatePortfolioHtml(PortfolioSummaryDto portfolio, LocalDate asOfDate) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .section { margin-bottom: 20px; }
                    table { width: 100%; border-collapse: collapse; margin-top: 10px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    .total { font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Portfolio Report</h1>
                    <p>PAN: %s</p>
                    <p>As of Date: %s</p>
                </div>
                <div class="section">
                    <h2>Portfolio Summary</h2>
                    <table>
                        <tr><td>Client Name</td><td>%s</td></tr>
                        <tr><td>Scheme Name</td><td>%s</td></tr>
                        <tr><td>Total Units</td><td>%s</td></tr>
                        <tr><td>Total Investment</td><td>₹%s</td></tr>
                        <tr><td>Current Value</td><td>₹%s</td></tr>
                        <tr><td>Gain/Loss</td><td>₹%s</td></tr>
                        <tr><td>Gain/Loss %%</td><td>%s%%</td></tr>
                    </table>
                </div>
            </body>
            </html>
            """.formatted(
                portfolio.getPan(),
                asOfDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                portfolio.getClientName(),
                portfolio.getSchemeName(),
                portfolio.getTotalUnits(),
                portfolio.getTotalInvestment(),
                portfolio.getCurrentValue(),
                portfolio.getGainLoss(),
                portfolio.getGainLossPercentage()
            );
    }

    private String generateTransactionHtml(List<TransactionReportDto> transactions, String pan, LocalDate fromDate, LocalDate toDate) {
        StringBuilder rows = new StringBuilder();
        for (TransactionReportDto txn : transactions) {
            rows.append(String.format("""
                <tr>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>₹%s</td>
                    <td>%s</td>
                    <td>₹%s</td>
                </tr>
                """,
                txn.getTxnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                txn.getTxnType(),
                txn.getSchemeName(),
                txn.getFolioNo(),
                txn.getTxnAmount(),
                txn.getTxnUnits(),
                txn.getTxnNav()
            ));
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    table { width: 100%; border-collapse: collapse; margin-top: 10px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Transaction Report</h1>
                    <p>PAN: %s</p>
                    <p>Period: %s to %s</p>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Type</th>
                            <th>Scheme</th>
                            <th>Folio</th>
                            <th>Amount</th>
                            <th>Units</th>
                            <th>NAV</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>
            </body>
            </html>
            """.formatted(
                pan,
                fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                rows.toString()
            );
    }

    private String generateSipHtml(Map<String, Object> sipSummary, List<SipPlan> activeSips, String pan) {
        StringBuilder rows = new StringBuilder();
        for (SipPlan sip : activeSips) {
            rows.append(String.format("""
                <tr>
                    <td>%s</td>
                    <td>₹%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                </tr>
                """,
                sip.getSchemeId(),
                sip.getSipAmount(),
                sip.getFrequency(),
                sip.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                sip.getCompletedInstallments(),
                sip.getStatus()
            ));
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    table { width: 100%; border-collapse: collapse; margin-top: 10px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>SIP Report</h1>
                    <p>PAN: %s</p>
                </div>
                <div class="section">
                    <h2>SIP Summary</h2>
                    <p>Active SIPs: %s</p>
                    <p>Total Active Amount: ₹%s</p>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Scheme ID</th>
                            <th>Amount</th>
                            <th>Frequency</th>
                            <th>Start Date</th>
                            <th>Completed</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>
            </body>
            </html>
            """.formatted(
                pan,
                sipSummary.get("activeSipCount"),
                sipSummary.get("totalActiveSipAmount"),
                rows.toString()
            );
    }

    private String generateCapitalGainsHtml(String pan, LocalDate fromDate, LocalDate toDate) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { text-align: center; margin-bottom: 30px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Capital Gains Report</h1>
                    <p>PAN: %s</p>
                    <p>Period: %s to %s</p>
                </div>
                <p>Capital gains calculation report will be implemented based on FIFO calculations.</p>
            </body>
            </html>
            """.formatted(
                pan,
                fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
    }

    private String generateClientStatementHtml(PortfolioSummaryDto portfolio, List<TransactionReportDto> transactions, 
                                             String pan, LocalDate fromDate, LocalDate toDate) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .section { margin-bottom: 20px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Client Statement</h1>
                    <p>PAN: %s</p>
                    <p>Period: %s to %s</p>
                </div>
                <div class="section">
                    <h2>Portfolio Summary</h2>
                    <p>Current Value: ₹%s</p>
                    <p>Total Gain/Loss: ₹%s</p>
                </div>
                <div class="section">
                    <h2>Transaction Summary</h2>
                    <p>Total Transactions: %d</p>
                </div>
            </body>
            </html>
            """.formatted(
                pan,
                fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                portfolio.getCurrentValue(),
                portfolio.getGainLoss(),
                transactions.size()
            );
    }
}
