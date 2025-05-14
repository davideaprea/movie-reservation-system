package com.example.demo.booking.repository;

import com.example.demo.booking.entity.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentDao extends CrudRepository<Payment, Long> {
}
