package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.exception.ResourceNotFoundException;
import com.rtd.finance_backend.exception.ValidationException;
import com.rtd.finance_backend.model.SipPlan;
import com.rtd.finance_backend.repository.SipPlanRepository;
import com.rtd.finance_backend.service.FifoService;
import com.rtd.finance_backend.service.SipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SipServiceImpl implements SipService {

    private final SipPlanRepository sipPlanRepository;
    private final FifoService fifoService;

    @Override
    @Transactional
    public SipPlan createSipPlan(SipPlan sipPlan) {
        // Check if there's already an active SIP for the same PAN and scheme
        if (sipPlanRepository.findActiveSipByPanAndScheme(sipPlan.getPan(), sipPlan.getSchemeId()).isPresent()) {
            throw new ValidationException("Active SIP already exists for PAN: " + sipPlan.getPan() + " and Scheme: " + sipPlan.getSchemeId());
        }
        
        return sipPlanRepository.save(sipPlan);
    }

    @Override
    @Transactional
    public SipPlan updateSipPlan(Long sipId, SipPlan sipPlan) {
        SipPlan existingSip = sipPlanRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP Plan", "id", sipId));
        
        existingSip.setSipAmount(sipPlan.getSipAmount());
        existingSip.setFrequency(sipPlan.getFrequency());
        existingSip.setEndDate(sipPlan.getEndDate());
        existingSip.setTotalInstallments(sipPlan.getTotalInstallments());
        
        return sipPlanRepository.save(existingSip);
    }

    @Override
    @Transactional
    public void cancelSipPlan(Long sipId) {
        SipPlan sipPlan = sipPlanRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP Plan", "id", sipId));
        
        sipPlan.setStatus(SipPlan.Status.CANCELLED);
        sipPlanRepository.save(sipPlan);
    }

    @Override
    @Transactional
    public void pauseSipPlan(Long sipId) {
        SipPlan sipPlan = sipPlanRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP Plan", "id", sipId));
        
        sipPlan.setStatus(SipPlan.Status.PAUSED);
        sipPlanRepository.save(sipPlan);
    }

    @Override
    @Transactional
    public void resumeSipPlan(Long sipId) {
        SipPlan sipPlan = sipPlanRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP Plan", "id", sipId));
        
        sipPlan.setStatus(SipPlan.Status.ACTIVE);
        sipPlanRepository.save(sipPlan);
    }

    @Override
    public SipPlan getSipPlanById(Long sipId) {
        return sipPlanRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP Plan", "id", sipId));
    }

    @Override
    public List<SipPlan> getSipPlansByPan(String pan) {
        return sipPlanRepository.findByPan(pan);
    }

    @Override
    public List<SipPlan> getActiveSipPlansByPan(String pan) {
        return sipPlanRepository.findByPanAndStatus(pan, SipPlan.Status.ACTIVE);
    }

    @Override
    public List<SipPlan> getSipPlansByScheme(Integer schemeId) {
        return sipPlanRepository.findBySchemeId(schemeId);
    }

    @Override
    @Transactional
    public void processSipInvestments(LocalDate date) {
        List<SipPlan> dueSips = sipPlanRepository.findActiveSipsDueForInvestment(date);
        
        for (SipPlan sip : dueSips) {
            try {
                // Create purchase lot for SIP investment
                fifoService.createPurchaseLot(
                    sip.getPan(),
                    sip.getSchemeId(),
                    sip.getFolioNo(),
                    date,
                    BigDecimal.valueOf(100.0), // Default NAV for SIP
                    sip.getSipAmount().divide(BigDecimal.valueOf(100.0), 4, java.math.RoundingMode.HALF_UP),
                    sip.getSipAmount()
                );
                
                // Update SIP plan
                sip.setCompletedInstallments(sip.getCompletedInstallments() + 1);
                sip.setTotalInvestedAmount(sip.getTotalInvestedAmount().add(sip.getSipAmount()));
                sip.setNextInvestmentDate(calculateNextInvestmentDate(sip.getNextInvestmentDate(), sip.getFrequency()));
                
                // Check if SIP is completed
                if (sip.getTotalInstallments() != null && 
                    sip.getCompletedInstallments() >= sip.getTotalInstallments()) {
                    sip.setStatus(SipPlan.Status.COMPLETED);
                }
                
                sipPlanRepository.save(sip);
                
                log.info("SIP investment processed for PAN: {}, Amount: {}", sip.getPan(), sip.getSipAmount());
                
            } catch (Exception e) {
                log.error("Failed to process SIP investment for PAN: {}", sip.getPan(), e);
            }
        }
    }

    @Override
    public Map<String, Object> getSipSummary(String pan) {
        Map<String, Object> summary = new HashMap<>();
        
        List<SipPlan> activeSips = getActiveSipPlansByPan(pan);
        BigDecimal totalAmount = getTotalActiveSipAmount(pan);
        Long sipCount = getActiveSipCount(pan);
        
        summary.put("activeSipCount", sipCount);
        summary.put("totalActiveSipAmount", totalAmount);
        summary.put("activeSips", activeSips);
        
        return summary;
    }

    @Override
    public BigDecimal getTotalActiveSipAmount(String pan) {
        BigDecimal amount = sipPlanRepository.getTotalActiveSipAmountByPan(pan);
        return amount != null ? amount : BigDecimal.ZERO;
    }

    @Override
    public Long getActiveSipCount(String pan) {
        return sipPlanRepository.countActiveSipsByPan(pan);
    }

    private LocalDate calculateNextInvestmentDate(LocalDate currentDate, String frequency) {
        switch (frequency) {
            case "DAILY":
                return currentDate.plusDays(1);
            case "WEEKLY":
                return currentDate.plusWeeks(1);
            case "MONTHLY":
                return currentDate.plusMonths(1);
            case "QUARTERLY":
                return currentDate.plusMonths(3);
            default:
                return currentDate.plusMonths(1);
        }
    }
}
