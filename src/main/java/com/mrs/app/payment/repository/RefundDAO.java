package com.mrs.app.payment.repository;

import com.mrs.app.payment.entity.Refund;
import org.springframework.data.repository.CrudRepository;

public interface RefundDAO extends CrudRepository<Refund, Long> {
}
