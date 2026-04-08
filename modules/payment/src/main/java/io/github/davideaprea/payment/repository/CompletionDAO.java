package io.github.davideaprea.payment.repository;

import io.github.davideaprea.payment.entity.Completion;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompletionDAO extends CrudRepository<Completion, Long> {
    Optional<Completion> findByIntentId(String intentId);
}
