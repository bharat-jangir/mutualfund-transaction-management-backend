package com.rtd.finance_backend.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void sendTransactionNotification(String to, String clientName, String transactionType, String amount);
    void sendPortfolioUpdateNotification(String to, String clientName, String portfolioValue);
    void sendSystemAlert(String to, String subject, String message);
}
