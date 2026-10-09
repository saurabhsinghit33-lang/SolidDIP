package com.example.dip.domain.port;

import com.example.dip.domain.model.BankTransaction;

public interface ComplianceAuditPublisher {
    void publishAuditEvent(BankTransaction transaction);
}
