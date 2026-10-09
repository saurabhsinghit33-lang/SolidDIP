package com.example.dip.infrastructure.messaging;

import com.example.dip.domain.model.BankTransaction;
import com.example.dip.domain.port.ComplianceAuditPublisher;
import org.springframework.stereotype.Component;

@Component
public class KafkaCompliancePublisher implements ComplianceAuditPublisher {

    @Override
    public void publishAuditEvent(BankTransaction transaction) {
        System.out.println("[KAFKA] Emitted transaction event to topic 'bfsi.audit.stream': " + transaction.transactionId() 
                + " [Status: " + transaction.status() + "]");
    }
}
