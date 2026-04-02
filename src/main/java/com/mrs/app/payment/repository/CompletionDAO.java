package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Completion;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompletionDAO extends CrudRepository<Completion, Long> {
    Optional<Completion> findByIntentId(String intentId);
}
