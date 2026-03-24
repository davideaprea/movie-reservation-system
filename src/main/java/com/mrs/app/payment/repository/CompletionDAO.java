package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Completion;
import org.springframework.data.repository.CrudRepository;

public interface CompletionDAO extends CrudRepository<Completion, Long> {
    boolean existsByIntentId(long intentId);
}
