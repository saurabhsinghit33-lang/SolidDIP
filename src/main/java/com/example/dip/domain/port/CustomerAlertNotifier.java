package com.example.dip.domain.port;

import java.math.BigDecimal;

public interface CustomerAlertNotifier {
    void sendDebitAlert(String accountNumber, BigDecimal amount, String transactionId);
}
