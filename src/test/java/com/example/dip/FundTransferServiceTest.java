package com.example.dip.service;

import com.example.dip.domain.port.ComplianceAuditPublisher;
import com.example.dip.domain.port.CustomerAlertNotifier;
import com.example.dip.domain.port.LedgerRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class FundTransferServiceTest {

    @Test
    void transfer_WhenSufficientFunds_ShouldSucceed() {
        // 1. Create "fake" adapters for our ports (No MySQL or Kafka needed!)
        LedgerRepository ledgerMock = mock(LedgerRepository.class);
        ComplianceAuditPublisher auditMock = mock(ComplianceAuditPublisher.class);
        CustomerAlertNotifier alertMock = mock(CustomerAlertNotifier.class);

        // 2. Inject the fakes into our real core business service
        FundTransferService service = new FundTransferService(ledgerMock, auditMock, alertMock);

        // 3. Tell the fake database how to behave when asked to debit
        BigDecimal transferAmount = new BigDecimal("100.00");
        when(ledgerMock.executeDebit("ACC-1", transferAmount)).thenReturn(true);

        // 4. Execute the actual business logic
        service.transferFunds("ACC-1", "ACC-2", transferAmount);

        // 5. Verify the core domain orchestrated the database calls correctly
        verify(ledgerMock).executeDebit("ACC-1", transferAmount);
        verify(ledgerMock).executeCredit("ACC-2", transferAmount);

        System.out.println("✅ Core Domain Logic Tested Successfully!");
    }
}