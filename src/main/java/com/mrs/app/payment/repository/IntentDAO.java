package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Intent;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IntentDAO extends CrudRepository<Intent, Long> {
    Optional<Intent> findByOrderId(String orderId);
}
