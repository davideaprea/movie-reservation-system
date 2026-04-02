package com.mrs.app.order.dao;

import com.mrs.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
    void deleteAllByIntentIdIn(List<String> paymentsIds);
}
