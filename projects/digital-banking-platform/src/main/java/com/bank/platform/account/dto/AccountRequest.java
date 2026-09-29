package com.bank.platform.account.dto;

import com.bank.platform.account.model.AccountType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccountRequest(
        @NotNull UUID customerId,
        @NotNull AccountType type
) {
}
