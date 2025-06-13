package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentDao extends CrudRepository<Payment, Long> {
    @Transactional
    @Modifying
    @Query("""
            UPDATE Payment p
            SET p.status = com.mrs.app.booking.enumeration.PaymentStatus.COMPLETED
            WHERE p.orderId = :orderId AND
                p.user.id = :userId AND
                p.status = com.mrs.app.booking.enumeration.PaymentStatus.PENDING AND
                p.createdAt > :cutoff
            """)
    int markAsCompleted(String orderId, long userId, LocalDateTime cutoff);

    @Transactional
    @Modifying
    @Query("""
            UPDATE Payment p
            SET p.captureId = :captureId
            WHERE p.orderId = :orderId AND
                p.user.id = :userId AND
                p.captureId IS NULL
            """)
    int setCaptureId(String orderId, String captureId, long userId);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Payment p
            WHERE p.status = com.mrs.app.booking.enumeration.PaymentStatus.PENDING AND
                p.createdAt > :cutoff
            """)
    void deleteExpiredUncompletedPayments(LocalDateTime cutoff);

    @Modifying
    @Query("""
            UPDATE Payment p
            SET p.status = com.mrs.app.booking.enumeration.PaymentStatus.REFUNDED
            WHERE p.id = :paymentId AND p.user.id = :userId
            """)
    int markAsRefunded(long paymentId, long userId);

    Optional<Payment> findByIdAndUserId(long id, long userId);
}
