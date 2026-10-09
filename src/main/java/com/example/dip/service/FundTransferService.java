package com.example.dip.service;

import com.example.dip.domain.model.BankTransaction;
import com.example.dip.domain.model.TransactionStatus;
import com.example.dip.domain.port.ComplianceAuditPublisher;
import com.example.dip.domain.port.CustomerAlertNotifier;
import com.example.dip.domain.port.LedgerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FundTransferService {

    private final LedgerRepository ledgerRepository;
    private final ComplianceAuditPublisher auditPublisher;
    private final CustomerAlertNotifier alertNotifier;

    public FundTransferService(LedgerRepository ledgerRepository,
                               ComplianceAuditPublisher auditPublisher,
                               CustomerAlertNotifier alertNotifier) {
        this.ledgerRepository = ledgerRepository;
        this.auditPublisher = auditPublisher;
        this.alertNotifier = alertNotifier;
    }

    public BankTransaction transferFunds(String sourceAccount, String destinationAccount, BigDecimal amount) {
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);

        boolean debitSuccess = ledgerRepository.executeDebit(sourceAccount, amount);
        if (!debitSuccess) {
            BankTransaction failedTxn = new BankTransaction(
                    transactionId, sourceAccount, destinationAccount, amount, TransactionStatus.FAILED, LocalDateTime.now()
            );
            auditPublisher.publishAuditEvent(failedTxn);
            return failedTxn;
        }

        ledgerRepository.executeCredit(destinationAccount, amount);

        BankTransaction settledTxn = new BankTransaction(
                transactionId, sourceAccount, destinationAccount, amount, TransactionStatus.SETTLED, LocalDateTime.now()
        );

        auditPublisher.publishAuditEvent(settledTxn);
        alertNotifier.sendDebitAlert(sourceAccount, amount, transactionId);

        return settledTxn;
    }
}
