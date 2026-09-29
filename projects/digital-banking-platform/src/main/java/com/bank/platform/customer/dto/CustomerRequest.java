package com.bank.platform.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String nationalId,
        @NotBlank @Email String email,
        @NotBlank String phoneNumber,
        @NotBlank String username
) {
}
