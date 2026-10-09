package com.example.dip.infrastructure.persistence;

import com.example.dip.domain.port.LedgerRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLedgerRepository implements LedgerRepository {

    private final Map<String, BigDecimal> accountBalances = new ConcurrentHashMap<>();

    public InMemoryLedgerRepository() {
        accountBalances.put("ACC-1001", new BigDecimal("50000.00"));
        accountBalances.put("ACC-2002", new BigDecimal("10000.00"));
    }

    @Override
    public boolean executeDebit(String accountNumber, BigDecimal amount) {
        BigDecimal balance = accountBalances.getOrDefault(accountNumber, BigDecimal.ZERO);
        if (balance.compareTo(amount) < 0) {
            System.out.println("[LEDGER] Debit rejected: Insufficient balance in " + accountNumber);
            return false;
        }
        accountBalances.put(accountNumber, balance.subtract(amount));
        System.out.println("[LEDGER] Debited $" + amount + " from " + accountNumber + ". New balance: $" + accountBalances.get(accountNumber));
        return true;
    }

    @Override
    public void executeCredit(String accountNumber, BigDecimal amount) {
        BigDecimal balance = accountBalances.getOrDefault(accountNumber, BigDecimal.ZERO);
        accountBalances.put(accountNumber, balance.add(amount));
        System.out.println("[LEDGER] Credited $" + amount + " to " + accountNumber + ". New balance: $" + accountBalances.get(accountNumber));
    }
}
