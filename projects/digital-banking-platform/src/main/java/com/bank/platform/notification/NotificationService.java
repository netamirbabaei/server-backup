package com.bank.platform.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Stub notification service. In a real microservice split this would publish
 * an event (Kafka/RabbitMQ) that a dedicated notification-service consumes,
 * instead of doing the work in-process.
 */
@Slf4j
@Service
public class NotificationService {

    public void notifyTransaction(UUID accountId, String type, BigDecimal amount) {
        log.info("[NOTIFICATION] account={} type={} amount={}", accountId, type, amount);
    }
}
