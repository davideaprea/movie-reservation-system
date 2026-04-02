package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Intent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IntentDAO extends CrudRepository<Intent, Long> {
    Optional<Intent> findByOrderId(String orderId);

    @Query("""
            SELECT i
            FROM Intent i
            LEFT JOIN Completion c ON c.intent = i
            WHERE i.expiresAt < CURRENT_TIMESTAMP AND c.id IS NULL
            """)
    List<Intent> findAllExpired();
}
