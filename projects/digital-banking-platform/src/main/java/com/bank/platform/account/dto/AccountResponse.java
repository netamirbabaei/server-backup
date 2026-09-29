package com.bank.platform.account.dto;

import com.bank.platform.account.model.Account;
import com.bank.platform.account.model.AccountStatus;
import com.bank.platform.account.model.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String accountNumber,
        UUID customerId,
        AccountType type,
        AccountStatus status,
        BigDecimal balance,
        String currency
) {
    public static AccountResponse from(Account a) {
        return new AccountResponse(a.getId(), a.getAccountNumber(), a.getCustomerId(),
                a.getType(), a.getStatus(), a.getBalance(), a.getCurrency());
    }
}
