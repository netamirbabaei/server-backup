package com.bank.platform.customer;

import com.bank.platform.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByNationalId(String nationalId);
    boolean existsByNationalId(String nationalId);
    boolean existsByEmail(String email);
}
