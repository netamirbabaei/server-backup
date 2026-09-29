package com.bank.platform.transaction;

import com.bank.platform.account.AccountRepository;
import com.bank.platform.account.model.Account;
import com.bank.platform.account.model.AccountStatus;
import com.bank.platform.common.exception.BusinessException;
import com.bank.platform.common.exception.ResourceNotFoundException;
import com.bank.platform.notification.NotificationService;
import com.bank.platform.transaction.dto.DepositRequest;
import com.bank.platform.transaction.dto.TransactionResponse;
import com.bank.platform.transaction.dto.TransferRequest;
import com.bank.platform.transaction.dto.WithdrawRequest;
import com.bank.platform.transaction.model.Transaction;
import com.bank.platform.transaction.model.TransactionStatus;
import com.bank.platform.transaction.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;

    public TransactionResponse deposit(DepositRequest request) {
        Account account = lockedAccount(request.accountId());
        assertActive(account);

        account.setBalance(account.getBalance().add(request.amount()));
        accountRepository.save(account);

        Transaction tx = record(null, account.getId(), TransactionType.DEPOSIT,
                request.amount(), request.description());

        notificationService.notifyTransaction(account.getId(), "DEPOSIT", request.amount());
        return TransactionResponse.from(tx);
    }

    public TransactionResponse withdraw(WithdrawRequest request) {
        Account account = lockedAccount(request.accountId());
        assertActive(account);
        assertSufficientFunds(account, request.amount());

        account.setBalance(account.getBalance().subtract(request.amount()));
        accountRepository.save(account);

        Transaction tx = record(account.getId(), null, TransactionType.WITHDRAWAL,
                request.amount(), request.description());

        notificationService.notifyTransaction(account.getId(), "WITHDRAWAL", request.amount());
        return TransactionResponse.from(tx);
    }

    public TransactionResponse transfer(TransferRequest request) {
        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new BusinessException("Cannot transfer to the same account", HttpStatus.BAD_REQUEST);
        }

        // lock in a stable order to avoid deadlocks between concurrent transfers
        UUID first = request.fromAccountId().compareTo(request.toAccountId()) < 0
                ? request.fromAccountId() : request.toAccountId();
        UUID second = first.equals(request.fromAccountId()) ? request.toAccountId() : request.fromAccountId();
        lockedAccount(first);
        lockedAccount(second);

        Account from = lockedAccount(request.fromAccountId());
        Account to = lockedAccount(request.toAccountId());
        assertActive(from);
        assertActive(to);
        assertSufficientFunds(from, request.amount());

        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));
        accountRepository.save(from);
        accountRepository.save(to);

        Transaction tx = record(from.getId(), to.getId(), TransactionType.TRANSFER,
                request.amount(), request.description());

        notificationService.notifyTransaction(from.getId(), "TRANSFER_OUT", request.amount());
        notificationService.notifyTransaction(to.getId(), "TRANSFER_IN", request.amount());
        return TransactionResponse.from(tx);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> historyForAccount(UUID accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId)
                .stream().map(TransactionResponse::from).toList();
    }

    private Account lockedAccount(UUID id) {
        return accountRepository.findWithLockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    private void assertActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active: " + account.getAccountNumber(), HttpStatus.CONFLICT);
        }
    }

    private void assertSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient funds in account: " + account.getAccountNumber(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private Transaction record(UUID fromId, UUID toId, TransactionType type, BigDecimal amount, String description) {
        Transaction tx = new Transaction();
        tx.setFromAccountId(fromId);
        tx.setToAccountId(toId);
        tx.setType(type);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setReferenceCode(UUID.randomUUID().toString());
        return transactionRepository.save(tx);
    }
}
