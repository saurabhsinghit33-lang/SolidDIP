package com.example.dip.infrastructure.notification;

import com.example.dip.domain.port.CustomerAlertNotifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SmsCustomerAlertNotifier implements CustomerAlertNotifier {

    @Override
    public void sendDebitAlert(String accountNumber, BigDecimal amount, String transactionId) {
        System.out.println("[SMS-GATEWAY] SMS alert sent to registered owner of " + accountNumber 
                + ": Debited $" + amount + " ref: " + transactionId);
    }
}
