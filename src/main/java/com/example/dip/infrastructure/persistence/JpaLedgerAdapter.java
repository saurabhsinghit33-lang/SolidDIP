package com.example.dip.infrastructure.persistence;

import com.example.dip.domain.port.LedgerRepository;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class JpaLedgerAdapter implements LedgerRepository {

    private final JpaAccountRepository repository;

    public JpaLedgerAdapter(JpaAccountRepository repository) {
        this.repository = repository;
    }

    // --- NEW: Automatically seed the MySQL database on startup ---
    @PostConstruct
    public void seedDatabase() {
        if (repository.findById("ACC-1001").isEmpty()) {
            repository.save(new AccountEntity("ACC-1001", new BigDecimal("50000.00")));
            repository.save(new AccountEntity("ACC-2002", new BigDecimal("10000.00")));
            System.out.println("[DB-SEED] Created test accounts with initial $50,000 balance.");
        }
    }

    @Override
    public boolean executeDebit(String accountNumber, BigDecimal amount) {
        Optional<AccountEntity> optionalAccount = repository.findById(accountNumber);

        if (optionalAccount.isEmpty()) {
            System.out.println("[DB-LEDGER] Account not found: " + accountNumber);
            return false;
        }

        AccountEntity account = optionalAccount.get();
        BigDecimal newBalance = account.getBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("[DB-LEDGER] Debit rejected: Insufficient balance in " + accountNumber);
            return false;
        }

        account.setBalance(newBalance);
        repository.save(account);

        System.out.println("[DB-LEDGER] Debited $" + amount + " from " + accountNumber +
                ". New balance: $" + newBalance);
        return true;
    }

    @Override
    public void executeCredit(String accountNumber, BigDecimal amount) {
        AccountEntity account = repository.findById(accountNumber)
                .orElse(new AccountEntity(accountNumber, BigDecimal.ZERO));

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        repository.save(account);

        System.out.println("[DB-LEDGER] Credited $" + amount + " to " + accountNumber +
                ". New balance: $" + newBalance);
    }
}