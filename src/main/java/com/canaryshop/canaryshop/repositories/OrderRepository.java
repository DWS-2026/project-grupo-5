package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
