package com.example.dip.dto;

import com.example.dip.domain.model.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        String transactionId,
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        TransactionStatus status,
        LocalDateTime timestamp
) {}
