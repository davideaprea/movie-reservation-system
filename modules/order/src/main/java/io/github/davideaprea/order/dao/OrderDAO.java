package io.github.davideaprea.order.dao;

import io.github.davideaprea.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
    void deleteAllByIntentIdIn(List<String> paymentsIds);
}
