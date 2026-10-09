package com.example.dip.domain.port;

import java.math.BigDecimal;

public interface LedgerRepository {
    boolean executeDebit(String accountNumber, BigDecimal amount);
    void executeCredit(String accountNumber, BigDecimal amount);
}
