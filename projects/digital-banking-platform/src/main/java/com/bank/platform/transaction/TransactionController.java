package com.bank.platform.transaction;

import com.bank.platform.common.dto.ApiResponse;
import com.bank.platform.transaction.dto.DepositRequest;
import com.bank.platform.transaction.dto.TransactionResponse;
import com.bank.platform.transaction.dto.TransferRequest;
import com.bank.platform.transaction.dto.WithdrawRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.deposit(request)));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.withdraw(request)));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.transfer(request)));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> history(@PathVariable UUID accountId) {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.historyForAccount(accountId)));
    }
}
