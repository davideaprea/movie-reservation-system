package io.github.davideaprea.payment.repository;

import io.github.davideaprea.payment.entity.Refund;
import org.springframework.data.repository.CrudRepository;

public interface RefundDAO extends CrudRepository<Refund, Long> {
}
