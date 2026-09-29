package com.bank.platform.account;

import com.bank.platform.account.dto.AccountRequest;
import com.bank.platform.account.dto.AccountResponse;
import com.bank.platform.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> open(@Valid @RequestBody AccountRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(accountService.open(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(accountService.getById(id)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(ApiResponse.ok(accountService.getByCustomer(customerId)));
    }

    @PostMapping("/{id}/freeze")
    public ResponseEntity<ApiResponse<AccountResponse>> freeze(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(accountService.freeze(id)));
    }

    @PostMapping("/{id}/unfreeze")
    public ResponseEntity<ApiResponse<AccountResponse>> unfreeze(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(accountService.unfreeze(id)));
    }
}
