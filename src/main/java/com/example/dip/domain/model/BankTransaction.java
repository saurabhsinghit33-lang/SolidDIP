package com.example.dip.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankTransaction(
        String transactionId,
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        TransactionStatus status,
        LocalDateTime timestamp
) {}
