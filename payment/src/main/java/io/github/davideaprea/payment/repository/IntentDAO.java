package io.github.davideaprea.payment.repository;

import com.mrs.app.payment.entity.Intent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IntentDAO extends CrudRepository<Intent, String> {
    @Query("""
            SELECT i
            FROM Intent i
            LEFT JOIN Completion c ON c.intent = i
            WHERE i.expiresAt < CURRENT_TIMESTAMP AND c.id IS NULL
            """)
    List<Intent> findExpiredIntents();
}
