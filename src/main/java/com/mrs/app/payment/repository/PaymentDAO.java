package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Intent;
import org.springframework.data.repository.CrudRepository;

public interface PaymentDAO extends CrudRepository<Intent, Long> {
}
