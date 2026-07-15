package com.mrs.payment.repository;

import com.mrs.payment.entity.Completion;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompletionRepository extends CrudRepository<Completion, Long> {
    Optional<Completion> findByIntentId(String intentId);
}
