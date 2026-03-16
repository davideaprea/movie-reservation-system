package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentDAO extends CrudRepository<Payment, Long> {
}
