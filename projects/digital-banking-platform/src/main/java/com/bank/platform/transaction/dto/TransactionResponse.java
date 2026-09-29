package com.bank.platform.transaction.dto;

import com.bank.platform.transaction.model.Transaction;
import com.bank.platform.transaction.model.TransactionStatus;
import com.bank.platform.transaction.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID fromAccountId,
        UUID toAccountId,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        String referenceCode,
        Instant createdAt
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(t.getId(), t.getFromAccountId(), t.getToAccountId(),
                t.getType(), t.getStatus(), t.getAmount(), t.getReferenceCode(), t.getCreatedAt());
    }
}
