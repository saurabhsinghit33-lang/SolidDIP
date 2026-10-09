package com.example.dip.infrastructure.messaging;

import com.example.dip.domain.model.BankTransaction;
import com.example.dip.domain.port.ComplianceAuditPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaCompliancePublisher implements ComplianceAuditPublisher {

    private final KafkaTemplate<String, BankTransaction> kafkaTemplate;
    private static final String TOPIC = "bfsi.audit.stream";

    // Constructor Injection for Spring KafkaTemplate
    public KafkaCompliancePublisher(KafkaTemplate<String, BankTransaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAuditEvent(BankTransaction transaction) {
        // Push the payload to the actual Kafka broker
        kafkaTemplate.send(TOPIC, transaction.transactionId(), transaction);

        System.out.println("[KAFKA-REAL] Published event to broker topic '" + TOPIC + "': "
                + transaction.transactionId());
    }
}