package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

    @Override
    public void sendTransactionNotification(String to, String clientName, String transactionType, String amount) {
        String subject = "Transaction Notification - " + transactionType;
        String text = String.format(
            "Dear %s,\n\n" +
            "Your %s transaction for amount %s has been processed successfully.\n\n" +
            "Thank you for using our mutual fund services.\n\n" +
            "Best regards,\n" +
            "Finance Management Team",
            clientName, transactionType, amount
        );
        
        sendSimpleEmail(to, subject, text);
    }

    @Override
    public void sendPortfolioUpdateNotification(String to, String clientName, String portfolioValue) {
        String subject = "Portfolio Update Notification";
        String text = String.format(
            "Dear %s,\n\n" +
            "Your portfolio value has been updated to %s.\n\n" +
            "You can view your complete portfolio details by logging into your account.\n\n" +
            "Best regards,\n" +
            "Finance Management Team",
            clientName, portfolioValue
        );
        
        sendSimpleEmail(to, subject, text);
    }

    @Override
    public void sendSystemAlert(String to, String subject, String message) {
        String alertText = String.format(
            "System Alert\n\n" +
            "Subject: %s\n\n" +
            "Message: %s\n\n" +
            "This is an automated system alert. Please take necessary action if required.\n\n" +
            "Best regards,\n" +
            "System Administrator",
            subject, message
        );
        
        sendSimpleEmail(to, "System Alert - " + subject, alertText);
    }
}
