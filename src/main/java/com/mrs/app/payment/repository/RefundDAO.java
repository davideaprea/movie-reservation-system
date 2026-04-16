package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Refund;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefundDAO extends CrudRepository<Refund, Long> {
    Optional<Refund> findByCompletionId(String completionId);
}
