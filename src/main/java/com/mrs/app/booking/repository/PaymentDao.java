package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentDao extends CrudRepository<Payment, Long> {
    @Query("""
            SELECT p
            FROM Payment p
            WHERE p.orderId = :orderId AND
                p.user.id = :userId
            """)
    Optional<Payment> findByOrderIdAndUserId(String orderId, long userId);

    @Modifying
    @Query("""
            DELETE FROM Payment p
            WHERE p.captureId IS NULL AND
                p.createdAt < :cutoff
            """)
    void deleteExpiredUncompletedPayments(LocalDateTime cutoff);
}
