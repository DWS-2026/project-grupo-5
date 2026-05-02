package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
}
