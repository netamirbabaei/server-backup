package com.bank.platform.customer.dto;

import com.bank.platform.customer.model.Customer;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        String nationalId,
        String email,
        String phoneNumber,
        String username
) {
    public static CustomerResponse from(Customer c) {
        return new CustomerResponse(c.getId(), c.getFirstName(), c.getLastName(),
                c.getNationalId(), c.getEmail(), c.getPhoneNumber(), c.getUsername());
    }
}
