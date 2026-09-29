package com.bank.platform.account;

import com.bank.platform.account.dto.AccountRequest;
import com.bank.platform.account.dto.AccountResponse;
import com.bank.platform.account.model.Account;
import com.bank.platform.account.model.AccountStatus;
import com.bank.platform.common.exception.BusinessException;
import com.bank.platform.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    public AccountResponse open(AccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setCustomerId(request.customerId());
        account.setType(request.type());
        return AccountResponse.from(accountRepository.save(account));
    }

    @Transactional(readOnly = true)
    public AccountResponse getById(UUID id) {
        return AccountResponse.from(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getByCustomer(UUID customerId) {
        return accountRepository.findByCustomerId(customerId).stream().map(AccountResponse::from).toList();
    }

    public AccountResponse freeze(UUID id) {
        Account account = findOrThrow(id);
        account.setStatus(AccountStatus.FROZEN);
        return AccountResponse.from(account);
    }

    public AccountResponse unfreeze(UUID id) {
        Account account = findOrThrow(id);
        account.setStatus(AccountStatus.ACTIVE);
        return AccountResponse.from(account);
    }

    private Account findOrThrow(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder("IR");
        for (int i = 0; i < 16; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
