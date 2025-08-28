package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.dto.PortfolioSummaryDto;
import com.rtd.finance_backend.dto.TransactionReportDto;
import com.rtd.finance_backend.model.*;
import com.rtd.finance_backend.repository.*;
import com.rtd.finance_backend.service.ReportingService;
import com.rtd.finance_backend.service.FifoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    @Autowired
    private UploadedTransactionInfoRepository transactionRepository;
    
    @Autowired
    private ClientMasterRepository clientRepository;
    
    @Autowired
    private SchemeMasterRepository schemeRepository;
    
    @Autowired
    private AmcMasterRepository amcRepository;
    
    @Autowired
    private TransactionLotRepository lotRepository;
    
    @Autowired
    private FifoService fifoService;

    @Override
    public PortfolioSummaryDto getPortfolioSummary(String pan) {
        // Get client details
        ClientMaster client = clientRepository.findByPan(pan)
            .orElseThrow(() -> new RuntimeException("Client not found with PAN: " + pan));
        
        // Get all schemes for this PAN
        List<UploadedTransactionInfo> transactions = transactionRepository.findByPan(pan);
        
        if (transactions.isEmpty()) {
            return new PortfolioSummaryDto(pan, client.getName(), "", "", "", 
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 
                BigDecimal.ZERO, LocalDateTime.now());
        }
        
        // Group by scheme
        Map<Integer, List<UploadedTransactionInfo>> transactionsByScheme = transactions.stream()
            .collect(Collectors.groupingBy(UploadedTransactionInfo::getSchemeId));
        
        // Calculate portfolio summary for the first scheme (or aggregate all)
        Integer firstSchemeId = transactionsByScheme.keySet().iterator().next();
        return calculatePortfolioSummaryForScheme(pan, client.getName(), firstSchemeId);
    }

    @Override
    public List<PortfolioSummaryDto> getAllPortfolios() {
        List<PortfolioSummaryDto> portfolios = new ArrayList<>();
        
        // Get all unique PANs
        List<String> allPans = transactionRepository.findAll().stream()
            .map(UploadedTransactionInfo::getPan)
            .distinct()
            .collect(Collectors.toList());
        
        for (String pan : allPans) {
            try {
                portfolios.add(getPortfolioSummary(pan));
            } catch (Exception e) {
                // Skip portfolios with errors
                System.err.println("Error processing portfolio for PAN: " + pan + " - " + e.getMessage());
            }
        }
        
        return portfolios;
    }

    @Override
    public Map<String, BigDecimal> getPortfolioValueByScheme(String pan) {
        Map<String, BigDecimal> portfolioByScheme = new HashMap<>();
        
        List<UploadedTransactionInfo> transactions = transactionRepository.findByPan(pan);
        Map<Integer, List<UploadedTransactionInfo>> transactionsByScheme = transactions.stream()
            .collect(Collectors.groupingBy(UploadedTransactionInfo::getSchemeId));
        
        for (Map.Entry<Integer, List<UploadedTransactionInfo>> entry : transactionsByScheme.entrySet()) {
            Integer schemeId = entry.getKey();
            SchemeMaster scheme = schemeRepository.findById(schemeId).orElse(null);
            String schemeName = scheme != null ? scheme.getSchemeName() : "Unknown Scheme";
            
            BigDecimal totalUnits = fifoService.getTotalRemainingUnits(pan, schemeId);
            BigDecimal totalCostBasis = fifoService.getTotalCostBasis(pan, schemeId);
            
            // Assuming current NAV is the last transaction NAV (in real scenario, this would be fetched from external API)
            BigDecimal currentNav = entry.getValue().stream()
                .map(UploadedTransactionInfo::getTxnNav)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
            
            BigDecimal currentValue = totalUnits.multiply(currentNav);
            portfolioByScheme.put(schemeName, currentValue);
        }
        
        return portfolioByScheme;
    }

    @Override
    public List<TransactionReportDto> getTransactionHistory(String pan, LocalDate fromDate, LocalDate toDate) {
        List<UploadedTransactionInfo> transactions = transactionRepository.findByPanAndTxnDateBetween(pan, fromDate, toDate);
        
        return transactions.stream()
            .map(this::convertToTransactionReportDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<TransactionReportDto> getTransactionHistoryByScheme(Integer schemeId, LocalDate fromDate, LocalDate toDate) {
        List<UploadedTransactionInfo> transactions = transactionRepository.findBySchemeIdAndTxnDateBetween(schemeId, fromDate, toDate);
        
        return transactions.stream()
            .map(this::convertToTransactionReportDto)
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, BigDecimal> getTransactionSummaryByType(String pan, LocalDate fromDate, LocalDate toDate) {
        List<UploadedTransactionInfo> transactions = transactionRepository.findByPanAndTxnDateBetween(pan, fromDate, toDate);
        
        return transactions.stream()
            .collect(Collectors.groupingBy(
                UploadedTransactionInfo::getTxnType,
                Collectors.reducing(BigDecimal.ZERO, UploadedTransactionInfo::getTxnAmount, BigDecimal::add)
            ));
    }

    @Override
    public Map<String, BigDecimal> getAmcWiseInvestmentSummary() {
        Map<String, BigDecimal> amcSummary = new HashMap<>();
        
        List<UploadedTransactionInfo> allTransactions = transactionRepository.findAll();
        
        for (UploadedTransactionInfo transaction : allTransactions) {
            SchemeMaster scheme = schemeRepository.findById(transaction.getSchemeId()).orElse(null);
            if (scheme != null && scheme.getAmc() != null) {
                String amcName = scheme.getAmc().getAmcName();
                BigDecimal currentAmount = amcSummary.getOrDefault(amcName, BigDecimal.ZERO);
                amcSummary.put(amcName, currentAmount.add(transaction.getTxnAmount()));
            }
        }
        
        return amcSummary;
    }

    @Override
    public List<Map<String, Object>> getTopSchemesByInvestment() {
        List<Map<String, Object>> topSchemes = new ArrayList<>();
        
        Map<Integer, BigDecimal> schemeInvestments = new HashMap<>();
        List<UploadedTransactionInfo> allTransactions = transactionRepository.findAll();
        
        // Calculate total investment per scheme
        for (UploadedTransactionInfo transaction : allTransactions) {
            Integer schemeId = transaction.getSchemeId();
            BigDecimal currentAmount = schemeInvestments.getOrDefault(schemeId, BigDecimal.ZERO);
            schemeInvestments.put(schemeId, currentAmount.add(transaction.getTxnAmount()));
        }
        
        // Sort by investment amount and get top 10
        List<Map.Entry<Integer, BigDecimal>> sortedSchemes = schemeInvestments.entrySet().stream()
            .sorted(Map.Entry.<Integer, BigDecimal>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());
        
        for (Map.Entry<Integer, BigDecimal> entry : sortedSchemes) {
            Map<String, Object> schemeData = new HashMap<>();
            SchemeMaster scheme = schemeRepository.findById(entry.getKey()).orElse(null);
            
            schemeData.put("schemeId", entry.getKey());
            schemeData.put("schemeName", scheme != null ? scheme.getSchemeName() : "Unknown");
            schemeData.put("amcName", scheme != null && scheme.getAmc() != null ? scheme.getAmc().getAmcName() : "Unknown");
            schemeData.put("totalInvestment", entry.getValue());
            
            topSchemes.add(schemeData);
        }
        
        return topSchemes;
    }

    @Override
    public Map<String, Long> getClientCountByTaxStatus() {
        List<ClientMaster> allClients = clientRepository.findAll();
        
        return allClients.stream()
            .collect(Collectors.groupingBy(
                client -> client.getTaxStatus().name(),
                Collectors.counting()
            ));
    }

    @Override
    public List<Map<String, Object>> getTopClientsByInvestment() {
        List<Map<String, Object>> topClients = new ArrayList<>();
        
        Map<String, BigDecimal> clientInvestments = new HashMap<>();
        List<UploadedTransactionInfo> allTransactions = transactionRepository.findAll();
        
        // Calculate total investment per client
        for (UploadedTransactionInfo transaction : allTransactions) {
            String pan = transaction.getPan();
            BigDecimal currentAmount = clientInvestments.getOrDefault(pan, BigDecimal.ZERO);
            clientInvestments.put(pan, currentAmount.add(transaction.getTxnAmount()));
        }
        
        // Sort by investment amount and get top 10
        List<Map.Entry<String, BigDecimal>> sortedClients = clientInvestments.entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());
        
        for (Map.Entry<String, BigDecimal> entry : sortedClients) {
            Map<String, Object> clientData = new HashMap<>();
            ClientMaster client = clientRepository.findByPan(entry.getKey()).orElse(null);
            
            clientData.put("pan", entry.getKey());
            clientData.put("clientName", client != null ? client.getName() : "Unknown");
            clientData.put("totalInvestment", entry.getValue());
            
            topClients.add(clientData);
        }
        
        return topClients;
    }

    @Override
    public Map<String, BigDecimal> getCurrentNavByScheme() {
        Map<String, BigDecimal> currentNavs = new HashMap<>();
        
        List<SchemeMaster> allSchemes = schemeRepository.findAll();
        
        for (SchemeMaster scheme : allSchemes) {
            // Get the latest NAV for this scheme (in real scenario, this would be fetched from external API)
            List<UploadedTransactionInfo> schemeTransactions = transactionRepository.findBySchemeId(scheme.getSchemeId());
            BigDecimal latestNav = schemeTransactions.stream()
                .map(UploadedTransactionInfo::getTxnNav)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
            
            currentNavs.put(scheme.getSchemeName(), latestNav);
        }
        
        return currentNavs;
    }

    @Override
    public List<Map<String, Object>> getNavHistory(Integer schemeId, LocalDate fromDate, LocalDate toDate) {
        List<Map<String, Object>> navHistory = new ArrayList<>();
        
        List<UploadedTransactionInfo> transactions = transactionRepository.findBySchemeIdAndTxnDateBetween(schemeId, fromDate, toDate);
        
        // Group by date and get NAV for each date
        Map<LocalDate, BigDecimal> navByDate = transactions.stream()
            .collect(Collectors.groupingBy(
                UploadedTransactionInfo::getTxnDate,
                Collectors.collectingAndThen(
                    Collectors.maxBy(Comparator.comparing(UploadedTransactionInfo::getTxnNav)),
                    opt -> opt.map(UploadedTransactionInfo::getTxnNav).orElse(BigDecimal.ZERO)
                )
            ));
        
        for (Map.Entry<LocalDate, BigDecimal> entry : navByDate.entrySet()) {
            Map<String, Object> navData = new HashMap<>();
            navData.put("date", entry.getKey());
            navData.put("nav", entry.getValue());
            navHistory.add(navData);
        }
        
        // Sort by date
        navHistory.sort((a, b) -> ((LocalDate) a.get("date")).compareTo((LocalDate) b.get("date")));
        
        return navHistory;
    }

    // Helper methods
    private PortfolioSummaryDto calculatePortfolioSummaryForScheme(String pan, String clientName, Integer schemeId) {
        SchemeMaster scheme = schemeRepository.findById(schemeId).orElse(null);
        AmcMaster amc = scheme != null ? scheme.getAmc() : null;
        
        BigDecimal totalUnits = fifoService.getTotalRemainingUnits(pan, schemeId);
        BigDecimal totalCostBasis = fifoService.getTotalCostBasis(pan, schemeId);
        
        // Get current NAV (in real scenario, this would be fetched from external API)
        List<UploadedTransactionInfo> schemeTransactions = transactionRepository.findBySchemeId(schemeId);
        BigDecimal currentNav = schemeTransactions.stream()
            .map(UploadedTransactionInfo::getTxnNav)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal currentValue = totalUnits.multiply(currentNav);
        BigDecimal gainLoss = currentValue.subtract(totalCostBasis);
        BigDecimal gainLossPercentage = totalCostBasis.compareTo(BigDecimal.ZERO) > 0 
            ? gainLoss.divide(totalCostBasis, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
            : BigDecimal.ZERO;
        
        // Get folio number from transactions
        String folioNo = schemeTransactions.stream()
            .map(UploadedTransactionInfo::getFolioNo)
            .findFirst()
            .orElse("");
        
        return new PortfolioSummaryDto(
            pan, clientName, 
            scheme != null ? scheme.getSchemeName() : "Unknown",
            amc != null ? amc.getAmcName() : "Unknown",
            folioNo, totalUnits, totalCostBasis, currentValue, gainLoss, gainLossPercentage,
            LocalDateTime.now()
        );
    }

    private TransactionReportDto convertToTransactionReportDto(UploadedTransactionInfo transaction) {
        ClientMaster client = clientRepository.findByPan(transaction.getPan()).orElse(null);
        SchemeMaster scheme = schemeRepository.findById(transaction.getSchemeId()).orElse(null);
        AmcMaster amc = scheme != null ? scheme.getAmc() : null;
        
        return new TransactionReportDto(
            transaction.getPan(),
            client != null ? client.getName() : "Unknown",
            scheme != null ? scheme.getSchemeName() : "Unknown",
            amc != null ? amc.getAmcName() : "Unknown",
            transaction.getTxnType(),
            transaction.getTxnDate(),
            transaction.getTxnUnits(),
            transaction.getTxnAmount(),
            transaction.getTxnNav(),
            transaction.getFolioNo(),
            transaction.getTaxStatus().name()
        );
    }
}
