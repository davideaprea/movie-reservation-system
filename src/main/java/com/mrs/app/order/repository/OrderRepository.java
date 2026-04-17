package com.mrs.app.order.repository;

import com.mrs.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    void deleteAllByIntentIdIn(List<String> paymentsIds);

    List<Order> findAllByUserId(long userId);
}
