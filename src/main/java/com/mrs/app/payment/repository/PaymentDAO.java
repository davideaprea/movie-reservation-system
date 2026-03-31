package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentDAO extends CrudRepository<Payment, Long> {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
