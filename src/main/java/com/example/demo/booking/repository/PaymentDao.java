package com.example.demo.booking.repository;

import com.example.demo.booking.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PaymentDao extends CrudRepository<Payment, Long> {
    @Modifying
    @Query("""
            UPDATE Payment p
            SET p.captureId = :captureId
            WHERE p.orderId = :orderId AND
            p.userId = :userId AND
            p.captureId IS NULL
            """)
    int confirm(String orderId, String captureId, long userId);
}
