package com.example.dip.infrastructure.messaging;

import com.example.dip.domain.model.BankTransaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAuditConsumer {

    @KafkaListener(topics = "bfsi.audit.stream", groupId = "bfsi-audit-group")
    public void consumeAuditEvent(BankTransaction transaction) {
        System.out.println("[KAFKA-CONSUMER] Successfully read transaction from broker: "
                + transaction.transactionId()
                + " | Source: " + transaction.sourceAccount()
                + " | Amount: $" + transaction.amount());
    }
}